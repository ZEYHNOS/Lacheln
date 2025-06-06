package aba3.lucid.sms.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.sms.business.SmsBusiness;
import aba3.lucid.sms.dto.SmsSendRequest;
import aba3.lucid.sms.dto.SmsSendResponse;
import aba3.lucid.sms.dto.SmsVerifyRequest;
import aba3.lucid.sms.dto.SmsVerifyResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsBusiness smsBusiness;

    // SMS 전송요청
    @PostMapping("/send")
    @Operation(summary = "인증용 코드 전화번호로 전송", description = "전화번호 인증 코드를 전송합니다.")
    public API<SmsSendResponse> sendCode(
            @RequestBody SmsSendRequest request
    )  {
        return smsBusiness.sendSms(request);
    }
    
    // SMS 코드 검증 요청
    @PostMapping("/verify")
    @Operation(summary = "인증용 코드 검증 결과 반환", description = "전화번호 인증 코드를 검증합니다.")
    public API<SmsVerifyResponse> verifyCode(
            @RequestBody SmsVerifyRequest request
    )  {
        return smsBusiness.verifySms(request);
    }
}
