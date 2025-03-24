package aba3.lucid.controller;

import aba3.lucid.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GateWayController {

    private final WebClient webClient;
    private final AuthService authService;

    // 유저 서비스로 요청을 전달하는 메서드
    @GetMapping("/user/**")
    public ResponseEntity<String> routeToUser(HttpServletRequest request) {
        return routeRequest("http://localhost:5051", request);
    }

    // 게시판 요청을 전달하는 메서드
    @GetMapping("/board/**")
    public ResponseEntity<String> routeToBoard(HttpServletRequest request, @AuthenticationPrincipal UserDetails user) {
        System.out.println("user.getUsername() + user.getPassword() + user.getAuthorities() = " + user.getUsername() + user.getPassword() + user.getAuthorities());
        return routeRequest("http://localhost:5052", request);
    }

    // 업체 서비스로 요청을 전달하는 메서드
    @GetMapping("/company/**")
    public ResponseEntity<String> routeToCompany(HttpServletRequest request) {
        return routeRequest("http://localhost:5052", request);
    }

    // 토큰 생성 메서드 (needs => userEmail, Role)
    @GetMapping("/addToken")
    public ResponseEntity<String> routeToLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, ResponseCookie> cookies = authService.login("user1@example.com");

        // 쿠키들을 Set-Cookie 헤더에 추가
        for (ResponseCookie cookie : cookies.values()) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        // 응답 본문 반환
        return ResponseEntity.ok("엑세스, 리프레시 토큰 생성 완료");
    }

    // 토큰 지우기 (needs => userEmail, Role)
    @GetMapping("/delToken")
    public ResponseEntity<String> routeToLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
        }

        Map<String, ResponseCookie> tokens = authService.logout(jwtToken);

        if(tokens != null)  {
            for (ResponseCookie cookie : tokens.values()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
        }
        
        return ResponseEntity.ok("토큰 삭제완료");
    }


    // 공통 HTTP 요청 전달 메서드
    private ResponseEntity<String> routeRequest(String baseUrl, HttpServletRequest request) {
        String path = request.getRequestURI();
        String url = baseUrl + path;

        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();

        Collections.list(request.getHeaderNames()).forEach(headerName ->
                headersMap.add(headerName, request.getHeader(headerName))
        );

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.addAll(headersMap))
                .retrieve()
                .toEntity(String.class)
                .block();
    }

}
