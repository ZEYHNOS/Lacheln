package aba3.lucid.email.service;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.email.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;
    private final EmailCodeService emailCodeService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    // 발신자 이메일 설정
    @Value("${spring.mail.username}")
    private String fromMail;

    public int sendVerificationCodeToEmail(String toMail)  {
        Optional<UsersEntity> user = usersRepository.findByUserEmail(toMail);
        Optional<CompanyEntity> company = companyRepository.findByCpEmail(toMail);
        int messageCode = 0;

        if(user.isEmpty() && company.isEmpty()) {
            String code = saveVerificationCode(toMail);
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                helper.setTo(toMail);
                helper.setFrom(fromMail);
                helper.setSubject("Lacheln 이메일 인증");
                helper.setText("<h3>인증 코드 : <b>" + code + "<b></h3>", true);

                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException("이메일 전송 실패 ㅠ");
            }
        } else {
            messageCode = 1;
        }
        return messageCode;
    }

    public String saveVerificationCode(String email)  {
        String code = emailCodeService.generateNumbericCode();
        emailVerificationRepository.saveVerificationCode(email, code);
        return code;
    }

    public int checkVerificationCode(String email, String code) {
        String savedCode = emailVerificationRepository.getVerificationCode(email);
        int messageCode;
        if(savedCode == null)    {
            messageCode = 1;
        } else if(!savedCode.equals(code)) {
            messageCode = 2;
        } else  {
            messageCode = 0;
            emailVerificationRepository.deleteVerificationCode(email);
        }

        return messageCode;
    }
}
