package aba3.lucid.sms.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.enums.SmsEnum;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.sms.dto.SmsSendRequest;
import aba3.lucid.sms.dto.SmsSendResponse;
import aba3.lucid.sms.dto.SmsVerifyRequest;
import aba3.lucid.sms.dto.SmsVerifyResponse;
import aba3.lucid.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Business
@RequiredArgsConstructor
public class SmsBusiness {

    private final SmsService smsService;

    public API<SmsSendResponse> sendSms(SmsSendRequest request) {
        boolean status = false;
        SmsEnum sendPhoneNum = smsService.sendCode(request.getPhoneNum());
        if(sendPhoneNum == SmsEnum.SEND_SUCCESS) {
            status = true;
        }
        SmsSendResponse response = SmsSendResponse.builder()
                .status(status)
                .phoneNum(request.getPhoneNum())
                .build();
        log.info("SendPhoneNum.getDescription() : {}", sendPhoneNum.getDescription());
        return status
                ? API.OK(response, sendPhoneNum.getDescription())
                : API.ERROR(response, ErrorCode.BAD_REQUEST, sendPhoneNum.getDescription());
    }

    public API<SmsVerifyResponse> verifySms(SmsVerifyRequest request) {
        boolean status = false;
        SmsEnum verifyCode = smsService.verifyCode(request.getPhoneNum(), request.getCode());
        if(verifyCode == SmsEnum.VERIFY_SUCCESS) {
            status = true;
        }
        SmsVerifyResponse response = SmsVerifyResponse.builder()
                .phoneNum(request.getPhoneNum())
                .verified(status)
                .build();
        return status
                ? API.OK(response, verifyCode.getDescription())
                : API.ERROR(response, ErrorCode.BAD_REQUEST, verifyCode.getDescription());
    }
}
