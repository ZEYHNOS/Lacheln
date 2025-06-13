package aba3.lucid.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.status_code.SuccessCode;
import aba3.lucid.domain.user.enums.TierEnum;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebServerController {

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
        return routeRequest("http://localhost:5052", request);
    }

    // 업체 서비스로 요청을 전달하는 메서드
    @RequestMapping(value = "/company/**", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.GET})
    public API<String> routeToCompany(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            String body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routeRequest("http://localhost:5051", request);
    }
    
    // JWT 기반은 필터에서 처리안함
    @PostMapping("/userlogout")
    @Operation(summary = "로그아웃", description = "사용자 세션을 제거하고 로그아웃 로직을 수행합니다.")
    public API<String> routeToLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("logout method processing!");

        // 새로운 쿠키 생성
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;

        // RefreshToken 추출(Redis에서 삭제진행을 위함)
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
        
        // RefreshToken과 함께 유효기간이 0인 쿠키생성
        Map<String, ResponseCookie> tokens = authService.logout(jwtToken);

        // 생성된 쿠키를 response에 저장 후 ContextHolder에 있는 세션 정보 삭제
        if(tokens != null)  {
            for (ResponseCookie cookie : tokens.values()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            log.info("Complete Set cookies : {}", tokens);
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
        }

        return API.OK(SuccessCode.DELETE_TOKEN);
    }

    // 회원 탈퇴(소비자, 업체) 로직
    @DeleteMapping("/delaccount")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    public API<String> delAccount(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException{
        // 로그아웃 로직과 매우 유사함
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

        // 해당 코드에서 알맞은 유저의 정보 DROP
        authService.withdrawUsers(accessToken, authentication);
        Map<String, ResponseCookie> tokens = authService.logout(jwtToken);

        if(tokens != null)  {
            for (ResponseCookie cookie : tokens.values()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
        }

        response.sendRedirect("http://localhost:3000");
        return API.OK(SuccessCode.DELETE_TOKEN);
    }

    // 세션 검증 메서드
    @GetMapping("/auth/me")
    @Operation(summary = "사용자 세션 검증", description = "사용자 세션을 검증합니다.")
    public API<Map<String, Object>> authMe(
            @AuthenticationPrincipal CustomUserDetails contextUser,
            HttpServletRequest requestUser
            )    {
        
        // 요청의 쿠키 받아오기
        Cookie[] cookies = requestUser.getCookies();

        // 담을 정보
        Map<String, Object> map = new HashMap<>();

        // 쿠키가 있을경우
        if(cookies != null) {
            // AccessToken 이라는 이름을 가진 쿠키찾기
            for (Cookie cookie : cookies) {
                if ("AccessToken".equals(cookie.getName())) {
                    // 토큰값 가져와서 검증하기
                    String token = cookie.getValue();
                    boolean valid = jwtTokenProvider.isValid(token);
                    if (valid) {
                        // 검증된 토큰으로 이메일 추출 및 컨텍스트에 있는 이메일 비교해서 사용자가 일치한지 검증
                        String getEmail = jwtTokenProvider.getUserEmail(token);
                        String sessionUser = contextUser.getEmail();
                        String role = contextUser.getRole();
                        String name = "";
                        TierEnum tier;

                        // 일치하면 true반환
                        if (getEmail.equals(sessionUser)) {
                            if(role.equals("USER") || role.equals("ADMIN")) {
                                name = authService.getUserNickName(getEmail);
                                tier = authService.getUserTier(getEmail);
                                if (role.equals("USER")) map.put("userId", contextUser.getUserId());
                            } else  {
                                name = authService.getCompanyName(getEmail);
                                tier = null;
                            }

                            map.put("tier", tier);
                            map.put("name", name);
                            map.put("valid", true);
                            map.put("role", role);

                            return API.OK(map, SuccessCode.SESSION_VALID);
                        }
                    }
                }
            }
        }
        map.put("name", null);
        map.put("valid", false);
        map.put("role", "ANONYMOUS");
        // 그 외의 경우 UNAUTHORIZED 반환
        return API.ERROR(map, ErrorCode.UNAUTHORIZED);
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
