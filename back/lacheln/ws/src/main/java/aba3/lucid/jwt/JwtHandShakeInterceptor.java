package aba3.lucid.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        // 쿠키에서 토큰 추출
        String token = getTokenFromCookies(request);
        log.info("JWT token: {}", jwtTokenProvider.isValid(token));
        return jwtTokenProvider.isValid(token);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 필요 시 후처리
    }

    // 쿠키에서 "AccessToken" 추출
    private String getTokenFromCookies(ServerHttpRequest  request) {
        List<String> cookies = request.getHeaders().get("Cookie");
        if (cookies != null) {
            String[] tokens = cookies.toString().split(";");
            for (String token : tokens) {
                if (token.contains("AccessToken")) {
                    String express = " AccessToken=";
                    if (token.startsWith(express)) {
                        log.info("AccessToken : {}", token);
                        System.out.println("RealToken : " + token.substring(express.length()).replaceAll("]", ""));
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

    // 토큰에서 사용자 ID 추출
    private String getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserId(token);  // 토큰에서 사용자 ID 추출
    }
}