package aba3.lucid.config;

import aba3.lucid.handler.LocalLoginFailureHandler;
import aba3.lucid.handler.LocalLoginSuccessHandler;
import aba3.lucid.handler.OAuth2LoginFailureHandler;
import aba3.lucid.handler.OAuth2LoginSuccessHandler;
import aba3.lucid.jwt.JwtAuthenticationFilter;
import aba3.lucid.jwt.JwtTokenProvider;
import aba3.lucid.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2UserService Oauth2UserService;
    private final OAuth2LoginSuccessHandler Oauth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler Oauth2LoginFailureHandler;
    private final LocalLoginSuccessHandler localLoginSuccessHandler;
    private final LocalLoginFailureHandler localLoginFailureHandler;

    private final String[] permitAlls = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
    private final String[] roleUser = {"/user/**", "/board/**"};
    private final String[] roleCompany = {"/company/**", "/product/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> { // TODO AbstractHttpConfigurer::disable
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*")); // TODO List.of("http://localhost:3000", "http://localhost:5050") <-- 넣어야함
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(permitAlls).permitAll()
//                        .requestMatchers(roleUser).permitAll() // TODO .hasRole("USER") 추가예정
//                        .requestMatchers(roleCompany).permitAll() // TODO .hasRole("COMPANY") 추가예정
                                .anyRequest().permitAll()// 그 외 모든 요청은 인증 필요
                )
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(Oauth2UserService))
                        .successHandler(Oauth2LoginSuccessHandler)
                        .failureHandler(Oauth2LoginFailureHandler))
                .formLogin(form -> form
                        .loginPage("/user/login")  // 로그인 페이지 직접 지정
                        .permitAll()
                        .successHandler(localLoginSuccessHandler)   // 로그인 성공 핸들러
                        .failureHandler(localLoginFailureHandler)  // 로그인 실패 핸들러
                        )
                .formLogin(form -> form
                        .loginPage("/company/login")  // 로그인 페이지 직접 지정
                        .permitAll()
                        .successHandler(localLoginSuccessHandler)   // 로그인 성공 핸들러
                        .failureHandler(localLoginFailureHandler)  // 로그인 실패 핸들러
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 적용
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}