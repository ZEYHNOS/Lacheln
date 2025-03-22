package aba3.lucid.service;

import aba3.lucid.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public String login(String userEmail)  {
        String accessToken = jwtTokenProvider.createAccessToken(userEmail, "USER");
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail, "USER");

        refreshTokenService.saveRefreshToken(userEmail, refreshToken);

        return accessToken;
    }

    public ResponseEntity<String> logout(HttpServletResponse response, String username) {
        System.out.println(username);
        refreshTokenService.deleteRefreshToken(username);
//         프론트에서 안한다하면 그냥 여기서 지우기
        ResponseCookie cookie = ResponseCookie.from("Authorization", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(HttpStatus.OK.toString());
    }

    // Access 토큰 재발급
    public String refreshAccessToken(String accessToken) {
        String accessEmail = jwtTokenProvider.getUserEmail(accessToken);
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
