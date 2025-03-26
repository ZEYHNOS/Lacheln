package aba3.lucid.config;

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
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserService Oauth2UserService;
    private final OAuth2LoginSuccessHandler Oauth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler Oauth2LoginFailureHandler;

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
                        .loginPage("/login")  // 로그인 페이지 직접 지정
                        .permitAll()
                        .defaultSuccessUrl("/home", true)  // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login")  // 로그인 실패 시 이동할 페이지
                        .successHandler((request, response, authentication) -> {
                            // 로그인 성공 시 추가 처리 (예: 성공 로그 출력 등)
                            response.sendRedirect("/home");  // 예: 홈 페이지로 리다이렉트
                        })
                        .failureHandler((request, response, exception) -> {
                            // 로그인 실패 시 추가 처리 (예: 실패 로그 출력 등)
                            response.sendRedirect("/login");  // 실패 시 로그인 페이지로 리다이렉트
                        }))
                .httpBasic(AbstractHttpConfigurer::disable);
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 적용
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}