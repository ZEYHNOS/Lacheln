package aba3.lucid.jwt;

import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.service.CustomUserDetailsService;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandShakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // WebSocketConfig의 registerStompEndpoints 메서드의 setInterceptor의 Chaining에 의한 토큰 검증 로직 및 유효성 검사
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        // 쿠키에서 토큰 추출
        String token = getTokenFromCookies(request);

        // 토큰 유효성 검증
        if(validateToken(token)) {
            // 유효한 토큰일 경우 사용자 정보 추출
            String userEmail = jwtTokenProvider.getUserEmail(token);
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // 인증 객체 생성
            Authentication auth = new CustomAuthenticationToken(userDetails, "", userDetails.getRole(), userDetails.getAuthorities());

            //StompHeaderAccess에 사용자 정보 추가
            attributes.put("user", auth); // WebSocket에 사용자 정보 추가!
            return true; // 인증 성공 및 사용자 정보 등록 완료.
        } else {
            return false; // 인증 실패
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 필요 시 후처리
    }

    // 쿠키에서 "AccessToken" 추출
    private String getTokenFromCookies(ServerHttpRequest  request) {
        // 헤더에서 쿠키에 해당하는 정보들 전부 추출
        List<String> cookies = request.getHeaders().get("Cookie");
        
        // 추출한 쿠키들이 있을 경우
        if (cookies != null) {
            // 토큰들 파싱하여 AccessToken의 쿠키값 안에서 Token값만 추출
            String[] tokens = cookies.toString().split(";");
            for (String token : tokens) {
                if (token.contains("AccessToken")) {
                    String express = " AccessToken=";
                    if (token.startsWith(express)) {
                        return token.substring(express.length()).replaceAll("]", ""); // "AccessToken=" 이후의 값만 반환
                    }
                    return null;
                }
            }
        }
        return null;  // "AccessToken" 쿠키가 없으면 null 반환
    }

    // 토큰의 유효성 검증 (예: 만료 여부 및 서명 유효성 검사)
    private boolean validateToken(String token) {
        return jwtTokenProvider.isValid(token);  // isExpired 대신 isValid로 변경
    }
}