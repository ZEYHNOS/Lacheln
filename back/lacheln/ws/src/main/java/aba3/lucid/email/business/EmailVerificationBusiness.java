package aba3.lucid.email.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.email.dto.EmailResponse;
import aba3.lucid.email.enums.EmailCodes;
import aba3.lucid.email.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

@Business
@RequiredArgsConstructor
public class EmailVerificationBusiness {

    private final EmailService emailService;

    // 요청된 이메일로 랜덤한 코드를 생성하여 이메일로 전송하는 로직
    public API<EmailResponse> sendEmail(String email) {
        // 해당하는 이메일로 코드전송
        EmailCodes messageCode = emailService.sendVerificationCodeToEmail(email);
        
        // 현 상황과 함께 결과 반환
        EmailResponse emailResponse = EmailResponse.builder()
                .message(messageCode)
                .build();
        if(messageCode.equals(EmailCodes.SEND_SUCCESS))    {
            return API.OK(emailResponse);
        }
        return API.ERROR(emailResponse, ErrorCode.BAD_REQUEST);
    }

    // 전송된 코드를 확인하는 로직
    public API<EmailResponse> verifyEmail(String email, String code) {
        // 요청한 이메일과, 사용자가 입력한 코드
        EmailCodes messageCode = emailService.checkVerificationCode(email, code);
        
        // 현 상황과 함께 결과 반환
        EmailResponse emailResponse = EmailResponse
                .builder()
                .message(messageCode)
                .build();
        if(messageCode.equals(EmailCodes.SUCCESS))    {
            return API.OK(emailResponse);
        }
        return API.ERROR(emailResponse, ErrorCode.BAD_REQUEST);
    }
}
