package aba3.lucid.sms.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.sms.dto.SmsSendRequest;
import aba3.lucid.sms.dto.SmsSendResponse;
import aba3.lucid.sms.dto.SmsVerifyRequest;
import aba3.lucid.sms.dto.SmsVerifyResponse;
import aba3.lucid.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.HttpStatusCode;

@Business
@RequiredArgsConstructor
public class SmsBusiness {

    private final SmsService smsService;

    public API<SmsSendResponse> sendSms(SmsSendRequest request) {
        String sendPhoneNum = smsService.sendCode(request.getPhoneNum());
        SmsSendResponse response = SmsSendResponse.builder()
                .phoneNum(sendPhoneNum)
                .build();
        return API.OK(response, "인증번호 전송에 완료, 핸드폰을 확인하여 인증번호를 입력해주세요.");
    }

    public API<SmsVerifyResponse> verifySms(SmsVerifyRequest request) {
        boolean verifyCode = smsService.verifyCode(request.getPhoneNum(), request.getCode());
        SmsVerifyResponse response = SmsVerifyResponse.builder()
                .phoneNum(request.getPhoneNum())
                .verified(verifyCode)
                .build();
        return response.getVerified()
                ? API.OK(response, "인증완료 되었습니다!")
                : API.ERROR(response, ErrorCode.CODE_NOT_MATCH, ErrorCode.CODE_NOT_MATCH.getDescription());
    }
}
