package aba3.lucid.sms.service;

import aba3.lucid.common.codegenerator.CodeGenerator;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.sms.repository.SmsRepository;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsService {

    @Value("${coolsms.api.key}")
    private String key;

    @Value("${coolsms.api.secret}")
    private String secret;

    @Value("${coolsms.api.sender}")
    private String sender;

    private final SmsRepository smsRepository;
    private final CodeGenerator codeGenerator;
    private DefaultMessageService defaultMessageService;

    public SmsService(SmsRepository smsRepository, CodeGenerator codeGenerator)  {
        this.smsRepository = smsRepository;
        this.codeGenerator = codeGenerator;
    }

    // @Value의 값들이 정의된 후 해당 생성자를 실행해야함
    @PostConstruct
    public void init() {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(key, secret, "https://api.coolsms.co.kr");
    }

    // SMS로 코드 전송
    public String sendCode(String phoneNumber)   {
        // 랜덤 코드 생성
        String verifyCode = codeGenerator.generateNumbericCode();

        // 메시지 생성
        Message message = new Message();

        // 메시지 발신자 설정
        message.setFrom(sender);
        
        // 메시지 수신자 설정
        message.setTo(phoneNumber);
        
        // 메시지 내용 설정
        message.setText("[발신] Lacheln 회원가입 인증 코드 : " + verifyCode);
    
        // 메시지 Response 설정
        SingleMessageSentResponse response = this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));

        // 메시지가 없을경우 예외처리
        if(response == null)    {
            throw new ApiException(ErrorCode.FAILED_SEND_CODE, ErrorCode.FAILED_SEND_CODE.getDescription());
        }

        System.out.println(response);

        // 예외가 없을 경우 전화번호를 key 코드를 value로 Redis에 저장
        smsRepository.saveVerificationCode(phoneNumber, verifyCode);

        return phoneNumber;
    }

    // 전송한 인증번호 검증
    public boolean verifyCode(String phoneNumber, String code) {
        // 결과값 초기화
        boolean result = false;
        
        // Redis에 저장된 코드 전화번호로 불러오기
        String savedCode = smsRepository.getVerificationCode(phoneNumber);
        
        // Redis에 해당 전화번호를 key값으로하는 코드가 없을 시 예외처리
        if(savedCode == null)    {
            throw new ApiException(ErrorCode.NOT_FOUND, "해당 번호로 전송된 인증번호가 존재하지 않습니다.");
        }

        // 저장된 코드와 사용자가 제출한 코드가 같을 시 true로 변환
        if(savedCode.equals(code))    {
            result = true;
            smsRepository.deleteVerificationCode(phoneNumber);
        }

        // 결과 값 return
        return result;
    }
}
