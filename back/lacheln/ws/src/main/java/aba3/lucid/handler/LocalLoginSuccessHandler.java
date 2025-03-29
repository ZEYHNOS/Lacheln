//package aba3.lucid.handler;
//
//import aba3.lucid.config.CustomUserDetails;
//import aba3.lucid.service.AuthService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class LocalLoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final AuthService authService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        System.out.println("LocalLoginSuccessHandler called");
//        String username = authentication.getName();
//
//        CustomUserDetails requestUser = (CustomUserDetails) authentication.getPrincipal();
//        System.out.println("requestUser = " + requestUser);
//        response.sendRedirect("http://localhost:3000");
//    }
//}
