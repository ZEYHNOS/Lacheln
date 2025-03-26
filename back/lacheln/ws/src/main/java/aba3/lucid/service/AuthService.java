package aba3.lucid.service;

import aba3.lucid.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    // 로그인 성공 시 토큰 발급
    public Map<String, ResponseCookie> login(String userEmail)  {
        String accessToken = jwtTokenProvider.createAccessToken(userEmail, "USER");
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail, "USER");
        Map<String, ResponseCookie> responseCookies = new HashMap<>();

        refreshTokenService.saveRefreshToken(userEmail, refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("AccessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(Duration.ofDays(10))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(Duration.ofDays(10))
                .build();

        responseCookies.put("AccessToken", accessCookie);
        responseCookies.put("RefreshToken", refreshCookie);

        return responseCookies;
    }

    // 빈 토큰정보를 쿠키에 넣어서 삭제
    public Map<String, ResponseCookie> logout(String refreshToken) {
        Map<String, ResponseCookie> responseCookies = new HashMap<>();
        String userEmail = jwtTokenProvider.getUserEmail(refreshToken);

        refreshTokenService.deleteRefreshToken(userEmail);

        ResponseCookie accessCookie = ResponseCookie.from("AccessToken", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(0)
                .build();

        responseCookies.put("AccessToken", accessCookie);
        responseCookies.put("RefreshToken", refreshCookie);

        return responseCookies;
    }

    // Access 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
        String accessEmail = jwtTokenProvider.getUserEmail(refreshToken);
        String role = jwtTokenProvider.getUserRole(refreshToken);

        // Redis에서 Refresh Token을 추출
        String savedRefreshToken = refreshTokenService.getRefreshToken(accessEmail);

        if (savedRefreshToken != null) {
            // Refresh Token이 유효하면 새로운 Access Token을 생성
            return jwtTokenProvider.createAccessToken(accessEmail, "USER");
        } else {
            throw new RuntimeException("Invalid or expired refresh token");
        }
    }
}
