package aba3.lucid.jwt;

import aba3.lucid.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    //SpringFilterChain메서드의 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)가 실행된 경우 해당 로직 실행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 정보를 추출
        Map<String, String> tokens = jwtTokenProvider.resolveTokens(request);
        String accessToken = tokens.get("AccessToken");
        String refreshToken = tokens.get("RefreshToken");
        
        // 토큰이 없는 경우는 생략
        if(accessToken != null && refreshToken != null)  {
            // AccessToken 만료 확인
            if(!jwtTokenProvider.isValid(accessToken)) {
                log.info("AceessToken 만료됨");
                // AccessToken이 만료되었을 시 재발급 로직
                accessToken = authService.refreshAccessToken(refreshToken);
                ResponseCookie accessCookie = ResponseCookie.from("AccessToken", accessToken)
                        .httpOnly(true)
                        .secure(true)
                        .sameSite("None") // 개발단계에서는 None 배포 시 strict
                        .path("/")
                        .maxAge(Duration.ofDays(10))
                        .build();
                response.setHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                log.info("Access Token 재발급 완료");
            }
            
            // 토큰에서 추출한 인증정보를 바탕으로 ContextHolder에 정보저장
            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        // 다음 프로세스 진행...
        filterChain.doFilter(request, response);
    }
}
