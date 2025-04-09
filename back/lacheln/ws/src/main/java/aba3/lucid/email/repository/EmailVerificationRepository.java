package aba3.lucid.email.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis에 코드를 저장
    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    // 이메일을 통해 코드를 반환받음
    public String getVerificationCode(String email) {
        return (String)redisTemplate.opsForValue().get(email);
    }

    // 인증 성공 시 필요없는 정보를 삭제
    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }
}
