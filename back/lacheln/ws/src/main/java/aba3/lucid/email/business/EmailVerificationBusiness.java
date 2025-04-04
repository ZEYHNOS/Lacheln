package aba3.lucid.email.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.email.dto.EmailResponse;
import aba3.lucid.email.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

@Business
@RequiredArgsConstructor
public class EmailVerificationBusiness {

    // 상황에 따른 메시지를 저장하는 리스트들
    private final String[] verifyMessages = {
            "인증이 완료 되었습니다!",
            "인증번호가 만료되었거나 존재하지 않습니다.",
            "잘못된 인증코드 입니다."
    };
    private final String[] sendMessage = {
            "인증코드 전송완료",
            "이미 가입된 이메일 입니다."
    };
    private final EmailService emailService;

    // 요청된 이메일로 랜덤한 코드를 생성하여 이메일로 전송하는 로직
    public API<EmailResponse> sendEmail(String email) {
        // 해당하는 이메일로 코드전송
        int messageCode = emailService.sendVerificationCodeToEmail(email);
        
        // 현 상황과 함께 결과 반환
        EmailResponse emailResponse = EmailResponse.builder()
                .message(sendMessage[messageCode])
                .build();
        if(messageCode == 0)    {
            return API.OK(emailResponse);
        }
        return API.ERROR(emailResponse, ErrorCode.BAD_REQUEST);
    }

    // 전송된 코드를 확인하는 로직
    public API<EmailResponse> verifyEmail(String email, String code) {
        // 요청한 이메일과, 사용자가 입력한 코드
        int messageCode = emailService.checkVerificationCode(email, code);
        
        // 현 상황과 함께 결과 반환
        EmailResponse emailResponse = EmailResponse
                .builder()
                .message(verifyMessages[messageCode])
                .build();
        if(messageCode == 0)    {
            return API.OK(emailResponse);
        }
        return API.ERROR(emailResponse, ErrorCode.BAD_REQUEST);
    }
}
