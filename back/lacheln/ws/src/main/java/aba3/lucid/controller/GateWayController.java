package aba3.lucid.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class GateWayController {

    private final WebClient webClient;

    // 유저 서비스로 요청을 전달하는 메서드
    @GetMapping("/user/**")
    public ResponseEntity<String> routeToUser(HttpServletRequest request) {
        return routeRequest("http://localhost:5051", request);
    }

    // 업체 서비스로 요청을 전달하는 메서드
    @GetMapping("/company/**")
    public ResponseEntity<String> routeToCompany(HttpServletRequest request) {
        return routeRequest("http://localhost:5052", request);
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
