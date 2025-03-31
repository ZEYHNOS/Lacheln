package aba3.lucid.email.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class EmailCodeService {

    // 6자리의 랜덤한 숫자만들기
    public String generateNumbericCode()    {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // 숫자 + 문자로 6자리 코드만들기
    public String generateNumStrCode()  {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i=0;i<6;i++)   {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}
