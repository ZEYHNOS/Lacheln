package aba3.lucid.email.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.email.business.EmailVerificationBusiness;
import aba3.lucid.email.dto.EmailSendRequest;
import aba3.lucid.email.dto.EmailResponse;
import aba3.lucid.email.dto.EmailVerifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationBusiness emailVerificationBusiness;

    // 6자리 코드 생성 후 이메일 전송까지
    @PostMapping("/send")
    @Operation(summary = "인증용 코드 이메일로 전송", description = "가입하고자하는 이메일로 인증용 코드를 전송합니다.")
    public API<EmailResponse> sendEmail(@RequestBody EmailSendRequest emailSendRequest)   {
        String email = emailSendRequest.getEmail();
        return emailVerificationBusiness.sendEmail(email);
    }

    // 전송된 인증용 코드와 사용자가 입력한 코드를 비교하여 인증 프로세스 진행
    @PostMapping("/verify")
    @Operation(summary = "두 코드 비교 후 인증", description = "인증용 코드와 사용자가 입력한 코드를 비교하여 인증 프로세스 진행합니다.")
    public API<EmailResponse> verifyEmail(@RequestBody EmailVerifyRequest emailVerifyRequest)   {
        String email = emailVerifyRequest.getEmail();
        String code = emailVerifyRequest.getCode();
        return emailVerificationBusiness.verifyEmail(email, code);
    }
}
