package aba3.lucid.email.controller;

import aba3.lucid.email.business.EmailVerificationBusiness;
import aba3.lucid.email.dto.EmailSendRequest;
import aba3.lucid.email.dto.EmailResponse;
import aba3.lucid.email.dto.EmailVerifyRequest;
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
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailSendRequest emailSendRequest)   {
        String email = emailSendRequest.getEmail();
        return emailVerificationBusiness.sendEmail(email);
    }

    @PostMapping("/verify")
    public ResponseEntity<EmailResponse> verifyEmail(@RequestBody EmailVerifyRequest emailVerifyRequest)   {
        String email = emailVerifyRequest.getEmail();
        String code = emailVerifyRequest.getCode();
        return emailVerificationBusiness.verifyEmail(email, code);
    }
}
