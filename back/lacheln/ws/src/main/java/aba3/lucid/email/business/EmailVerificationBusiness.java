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

    public API<EmailResponse> sendEmail(String email) {
        int messageCode = emailService.sendVerificationCodeToEmail(email);
        EmailResponse emailResponse = EmailResponse.builder()
                .message(sendMessage[messageCode])
                .build();
        if(messageCode == 0)    {
            return API.OK(emailResponse);
        }
        return API.ERROR(emailResponse, ErrorCode.BAD_REQUEST);
    }

    public API<EmailResponse> verifyEmail(String email, String code) {
        int messageCode = emailService.checkVerificationCode(email, code);
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
