package aba3.lucid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Refresh Token Redis에 저장
    public void saveRefreshToken(String userEmail, String refreshToken) {
        redisTemplate.opsForValue().set(userEmail, refreshToken, Duration.ofDays(7));
    }

    // Refresh Token Redis에서 조회
    public String getRefreshToken(String userEmail) {
        return redisTemplate.opsForValue().get(userEmail)+"";
    }

    // Refresh Token Redis에 저장
    public void deleteRefreshToken(String userEmail) {
        redisTemplate.delete(userEmail);
    }
}
