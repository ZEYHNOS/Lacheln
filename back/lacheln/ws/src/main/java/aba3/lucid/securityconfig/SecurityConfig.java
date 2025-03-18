package aba3.lucid.securityconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomCompanyDetailsService companyDetailsService;

    // WebFlux 방식으로 SecurityFilterChain 설정
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable()) // CORS 설정
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeExchange(auth -> auth
                        .pathMatchers("/user/**").hasRole("USER") // 사용자 관련 요청은 사용자 권한 필요
                        .pathMatchers("/company/**").hasRole("COMPANY") // 업체 관련 요청은 업체 권한 필요
                        .pathMatchers("/swagger-ui.html", "/webjars/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI 관련 요청은 인증 없이 허용
                        .anyExchange().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // 기본 로그인 폼 비활성화
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable); // HTTP 기본 인증 비활성화
        return http.build();
    }
// TODO 나중에 수정할것
//     WebFlux에서 인증을 처리하는 ReactiveAuthenticationManager 생성
//    @Bean
//    public ReactiveAuthenticationManager authenticationManager() {
//        return new UsernamePasswordReactiveAuthenticationManager(userDetailsService, companyDetailsService);
//    }
//
//    // WebFlux에서 CORS 설정
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
//        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
