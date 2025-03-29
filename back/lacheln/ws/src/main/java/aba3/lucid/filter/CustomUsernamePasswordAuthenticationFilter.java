package aba3.lucid.filter;

import aba3.lucid.config.CustomAuthenticationToken;
import aba3.lucid.jwt.JwtTokenProvider;
import aba3.lucid.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    @PostConstruct
    public void init()  {
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("attemptAuthentication called == > " + request.getContextPath());
        
        // POST 메서드 확인
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // Application/JSON으로 왔는지 확인
        if (!request.getContentType().equalsIgnoreCase("application/json")) {
            throw new AuthenticationServiceException("Content-Type must be application/json");
        }

        try {
            // JSON 요청 본문을 String으로 읽기
            String requestBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining("\n"));
            System.out.println("Request Body: " + requestBody);

            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            String role = jsonNode.get("role").asText();

            System.out.println("username = " + username);
            System.out.println("password = " + password);
            System.out.println("role = " + role);

            // CustomAuthenticationToken 생성 후 인증 시도
            CustomAuthenticationToken authRequest = new CustomAuthenticationToken(username, password, role);
            Authentication authResult = authenticationManager.authenticate(authRequest);

            // 인증이 되었으면 ContextHolder에 정보 저장 후 토큰 발급 후 넘겨주기
            if(authResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authResult);
                System.out.println("Local Authentication successful: " + authResult.getPrincipal());
            }

            return authResult;

        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse JSON request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();
        String role = authResult.getAuthorities().toString().replace("[ROLE_", "").replace("]", "");
        System.out.println("role = " + role);
        Map<String, ResponseCookie> cookies = authService.login(username, role);
        for (ResponseCookie cookie : cookies.values()) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        // CORS 헤더 추가
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // JSON 응답 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":true,\"message\":\"로그인 성공\"}");
    }
}