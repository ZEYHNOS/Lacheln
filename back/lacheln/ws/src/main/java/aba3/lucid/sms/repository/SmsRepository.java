package aba3.lucid.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class SmsRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveVerificationCode(String phoneNum, String code) {
        redisTemplate.opsForValue().set(phoneNum, code, 5, TimeUnit.MINUTES);
    }

    public String getVerificationCode(String phoneNum) {
        return (String)redisTemplate.opsForValue().get(phoneNum);
    }

    public void deleteVerificationCode(String phoneNum) {
        redisTemplate.delete(phoneNum);
    }
}
