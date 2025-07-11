package aba3.lucid.handler;

import aba3.lucid.config.GlobalConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    private final GlobalConfig globalConfig;
    // 소셜 로그인 실패 핸들러
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("OAuth2LoginFailureHandler called");
        System.out.println("로그인 실패: " + exception.getMessage());
        response.sendRedirect(globalConfig.getBaseUrl() + "/login");
    }
}
