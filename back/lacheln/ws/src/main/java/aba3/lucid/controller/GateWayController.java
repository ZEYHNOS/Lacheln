package aba3.lucid.controller;

import aba3.lucid.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/addToken")
    public ResponseEntity<String> routeToLogin() {
        String token = authService.login("user1@example.com");
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(75400)
                .build();
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(HttpStatus.OK.toString());
    }

    @GetMapping("/delToken")
    public ResponseEntity routeToLogout(HttpServletResponse response) {
        return authService.logout(response, "JohnDoe");
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
                .block(); // 동기 방식 처리
    }

}
