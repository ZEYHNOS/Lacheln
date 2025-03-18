package aba3.lucid.securityconfig;

import aba3.lucid.jwtconfig.JwtProperties;
import aba3.lucid.jwtconfig.JwtProvider;
import aba3.lucid.jwtconfig.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 사용자 및 업체측 인증을 위한 데이터
    private final CustomUserDetailsService userDetailsService;
    private final CustomCompanyDetailsService companyDetailsService;


    // HTTP 요청을 가로채 인증 여부 결정 및 인증 진행
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) // CORS 설정
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").hasRole("USER") // 사용자 관련 요청은 사용자 권한 필요
                        .requestMatchers("/company/**").hasRole("COMPANY") // 업체 관련 요청은 업체 권한 필요
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // Swagger UI 관련 요청은 인증 없이 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .httpBasic(AbstractHttpConfigurer::disable); // HTTP 기본 인증 비활성화
        return http.build();
    }

    // 인증 프로세스를 담당하는 Bean 생성(customAuthenticationProvider 클래스를 통해 사용자 또는 업체 어느 요청이 와도 인증 프로세스 원활하게 실행)
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }

//    // 추후 axios 미들웨어 사용 예정으로 cors 설정을 위한 Bean 등록, 초기상태는 모든것을 허용하게 설정
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용 (보안이 필요하면 특정 도메인만 허용)
//        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
//        configuration.addAllowedHeader("*"); // 모든 헤더 허용
//        configuration.setAllowCredentials(true); // 쿠키 요청 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
//
//        return source;
//    }


    // 비밀번호를 암호화 및 검증을 담당하는 객체
    @Bean
    public PasswordEncoder passwordEncoder()    {
        return new BCryptPasswordEncoder();
    }
}