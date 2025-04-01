package aba3.lucid.jwt;

import aba3.lucid.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> tokens = jwtTokenProvider.resolveTokens(request);
        String accessToken = tokens.get("AccessToken");
        String refreshToken = tokens.get("RefreshToken");
        if(accessToken != null && refreshToken != null)  {
            // Access 토큰 만료시 재발급
            if(jwtTokenProvider.isExpired(accessToken)) {
                accessToken = authService.refreshAccessToken(refreshToken);
                ResponseCookie accessCookie = ResponseCookie.from("AccessToken", accessToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("None") // 개발단계에서는 None 배포 시 strict
                        .path("/")
                        .maxAge(Duration.ofDays(10))
                        .build();
                response.setHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
            }
            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        System.out.println("JwtAuthentication Filter 다 빠져나옴");
        filterChain.doFilter(request, response);
    }
}
