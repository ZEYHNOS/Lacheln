package aba3.lucid.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.SuccessCode;
import aba3.lucid.jwt.JwtTokenProvider;
import aba3.lucid.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GateWayController {

    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    // 유저 서비스로 요청을 전달하는 메서드
    @RequestMapping("/user/**")
    public API<String> routeToUser(HttpServletRequest request) {
        return routeRequest("http://localhost:5052", request);
    }

    // 게시판 요청을 전달하는 메서드
    @RequestMapping("/board/**")
    public API<String> routeToBoard(HttpServletRequest request, @AuthenticationPrincipal UserDetails user) {
        System.out.println("user.getUsername() + user.getPassword() + user.getAuthorities() = " + user.getUsername() + user.getPassword() + user.getAuthorities());
        return routeRequest("http://localhost:5052", request);
    }

    // 업체 서비스로 요청을 전달하는 메서드
    @RequestMapping(value = "/company/**", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.GET})
    public API<String> routeToCompany(HttpServletRequest request) {
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
    @GetMapping("/userlogout")
    @Operation(summary = "로그아웃", description = "사용자 세션을 제거하고 로그아웃 로직을 수행합니다.")
    public API<String> routeToLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
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
            return API.ERROR(ErrorCode.BAD_REQUEST);
        }

        Map<String, ResponseCookie> tokens = authService.logout(jwtToken);

        if(tokens != null)  {
            for (ResponseCookie cookie : tokens.values()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                System.out.println("컨텍스트 정보 삭제완료..");
            }
        }

        response.sendRedirect("http://localhost:3000");
        return API.OK(SuccessCode.DELETE_TOKEN);
    }

    @DeleteMapping("/delaccount")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    public API<String> delAccount(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        log.info("delaccount called, {}");
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                } else if ("AccessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                }
            }
        }

        if (jwtToken == null) {
            return API.ERROR(ErrorCode.BAD_REQUEST);
        }

        authService.withdrawUsers(accessToken);
        Map<String, ResponseCookie> tokens = authService.logout(jwtToken);

        if(tokens != null)  {
            for (ResponseCookie cookie : tokens.values()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                System.out.println("컨텍스트 정보 삭제완료..");
            }
        }

        response.sendRedirect("http://localhost:3000");
        return API.OK(SuccessCode.DELETE_TOKEN);
    }

    // 공통 HTTP 요청 전달 메서드 (RestTemplate 사용)
    public API<String> routeRequest(String baseUrl, HttpServletRequest request) {
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
        String description = "path : " + path + "url : " + url + "method : " + method;

        String entity = new HttpEntity<>(body, headers).toString();
        return API.OK(entity, description);
    }
}
