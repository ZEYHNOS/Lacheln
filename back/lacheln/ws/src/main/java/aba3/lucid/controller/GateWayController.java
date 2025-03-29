package aba3.lucid.controller;

import aba3.lucid.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GateWayController {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    // 유저 서비스로 요청을 전달하는 메서드
    @GetMapping("/user/**")
    public ResponseEntity<String> routeToUser(HttpServletRequest request) {
        return routeRequest("http://localhost:5052", request);
    }

    // 게시판 요청을 전달하는 메서드
    @GetMapping("/board/**")
    public ResponseEntity<String> routeToBoard(HttpServletRequest request, @AuthenticationPrincipal UserDetails user) {
        System.out.println("user.getUsername() + user.getPassword() + user.getAuthorities() = " + user.getUsername() + user.getPassword() + user.getAuthorities());
        return routeRequest("http://localhost:5052", request);
    }

    // 업체 서비스로 요청을 전달하는 메서드
    @RequestMapping(value = "/company/**", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.GET})
    public ResponseEntity<String> routeToCompany(HttpServletRequest request) {
        System.out.println("===== routeToCompany 호출 =====");
        System.out.println("요청 URI: " + request.getRequestURI());
        System.out.println("요청 메서드: " + request.getMethod());

        try {
            BufferedReader reader = request.getReader();
            String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println("요청 본문: " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routeRequest("http://localhost:5051", request);
    }

    // 토큰 지우기 (needs => userEmail, Role)
    @GetMapping("/delToken")
    public ResponseEntity<String> routeToLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    // 공통 HTTP 요청 전달 메서드 (RestTemplate 사용)
    public ResponseEntity<String> routeRequest(String baseUrl, HttpServletRequest request) {
        String path = request.getRequestURI();
        String url = baseUrl + path;

        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                headers.add(headerName, request.getHeader(headerName))
        );

        HttpMethod method = HttpMethod.valueOf(request.getMethod());

        // Body 읽기
        String body = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, method, entity, String.class);
    }
}
