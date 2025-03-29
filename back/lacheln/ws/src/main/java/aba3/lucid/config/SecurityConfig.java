package aba3.lucid.config;

import aba3.lucid.filter.CustomUsernamePasswordAuthenticationFilter;
import aba3.lucid.handler.OAuth2LoginFailureHandler;
import aba3.lucid.handler.OAuth2LoginSuccessHandler;
import aba3.lucid.jwt.JwtAuthenticationFilter;
import aba3.lucid.jwt.JwtTokenProvider;
import aba3.lucid.service.AuthService;
import aba3.lucid.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final String[] permitAlls = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
    private final String[] roleUser = {"/user/**", "/board/**"};
    private final String[] roleCompany = {"/company/**", "/product/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, AuthService authService) throws Exception {
        CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager, authService);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> { // TODO AbstractHtt pConfigurer::disable
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // TODO List.of("http://localhost:3000", "http://localhost:5050") <-- 넣어야함
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
                .formLogin(form -> form.loginProcessingUrl("/login"))
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(Oauth2UserService))
                        .successHandler(Oauth2LoginSuccessHandler)
                        .failureHandler(Oauth2LoginFailureHandler))
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}