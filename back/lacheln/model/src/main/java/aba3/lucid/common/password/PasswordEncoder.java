package aba3.lucid.common.password;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordEncoder {
    public String encrypt(String email, String password) {
        try {
            //getSalt 메서드를 통해 이메일을 기반으로 salt를 생성하고, 이를 비밀번호 해싱에 사용합니다.
            KeySpec spec = new PBEKeySpec(password.toCharArray(), getSalt(email), 85319, 128);
            // PasswordEncoder 클래스는 PBKDF2 with HMAC-SHA1 알고리즘을 사용하여 비밀번호를 해시한다
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException |
                 InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getSalt(String email)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] keyBytes = email.getBytes("UTF-8");

        return digest.digest(keyBytes);
    }
}
