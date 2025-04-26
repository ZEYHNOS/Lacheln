package aba3.lucid.filter;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.config.CustomAuthenticationToken;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.service.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CompanyRepository companyRepository;
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, CompanyRepository companyRepository, UsersRepository usersRepository, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.companyRepository = companyRepository;
        this.usersRepository = usersRepository;
        this.authService = authService;
    }

    @PostConstruct
    public void init()  {
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
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

            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            String role = jsonNode.get("role").asText();

            Collection<GrantedAuthority> roles = List.of(
                    new SimpleGrantedAuthority("ROLE_"+role)
            );

            String userPk;
            Long cpPk;
            CustomAuthenticationToken authRequest = null;

            if(role.equals("USER")) {
                Optional<UsersEntity> user = usersRepository.findByUserEmail(username);
                if(user.isPresent()) {
                    userPk = user.get().getUserId();
                    authRequest = new CustomAuthenticationToken(username, password, roles, user.get().getUserRole(), userPk);
                } else {
                    throw new ApiException(ErrorCode.NOT_FOUND, "User not found");
                }
            } else if(role.equals("COMPANY")) {
                Optional<CompanyEntity> company = companyRepository.findByCpEmail(username);
                if(company.isPresent()) {
                    cpPk = company.get().getCpId();
                    authRequest = new CustomAuthenticationToken(username, password, roles, company.get().getCpRole(), cpPk);
                } else {
                    throw new ApiException(ErrorCode.NOT_FOUND, "Company not found");
                }
            }

            // CustomAuthenticationToken 생성 후 인증 시도 후 성공 시 ContextHolder에 세션 정보 저장
            Authentication authResult = authenticationManager.authenticate(authRequest);
            if(authResult.isAuthenticated()) {
                CustomAuthenticationToken result = (CustomAuthenticationToken) authResult;
                log.info("Authentication UserId : {}", result.getUserId());
                log.info("Authentication CompanyId : {}", result.getCompanyId());
                log.info("Authentication LoginType : {}", result.getLoginType());
                log.info("Authentication Name : {}", result.getName());
//                SecurityContextHolder.getContext().setAuthentication(authResult);
                log.info("Authentication Successful, {}", authResult.isAuthenticated());
            }
            return authResult;
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse JSON request body", e);
        }
    }

    // 인증에 성공했을때 핸들러
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomAuthenticationToken authRequest = (CustomAuthenticationToken) authResult;
        String username = authResult.getName();
        String role = authRequest.getLoginType().replace("ROLE_", "");
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