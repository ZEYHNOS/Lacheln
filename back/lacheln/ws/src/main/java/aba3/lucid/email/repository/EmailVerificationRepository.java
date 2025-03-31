package aba3.lucid.email.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    public String getVerificationCode(String email) {
        return (String)redisTemplate.opsForValue().get(email);
    }

    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }
}
