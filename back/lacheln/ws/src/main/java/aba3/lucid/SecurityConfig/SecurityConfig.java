package aba3.lucid.SecurityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(csrf -> csrf.disable()) // REST API를 이용한 서버임으로 csrf 보호 비활성화
                .authorizeHttpRequests(auth -> auth // 특정 패턴에 해당하는 url이 요청될 경우 사용자의 권한을 확인하여 인증 여부 및 인증 진행
                        .requestMatchers("/user/**").hasRole("USER") // 사용자측 요청이면 URL이 /user로 시작
                        .requestMatchers("/company/**").hasRole("COMPANY") // 업체측 요청이면 URL이 /company로 시작
                        .requestMatchers("/api/**").permitAll() // 해당하는 요청은 모든 사용자들이 접근 할 수 있도록 허가(추가 예정)
                        .anyRequest().authenticated() //  그 외 모든 요청에 대해서는 인증을 마친 사용자만 접근 할 수 있도록 설정
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // 인증 프로세스를 담당하는 Bean 생성(customAuthenticationProvider 클래스를 통해 사용자 또는 업체 어느 요청이 와도 인증 프로세스 원활하게 실행)
    @Bean
    public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        return new ProviderManager(List.of(customAuthenticationProvider));
    }

    // 추후 axios 미들웨어 사용 예정으로 cors 설정을 위한 Bean 등록, 초기상태는 모든것을 허용하게 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용 (보안이 필요하면 특정 도메인만 허용)
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }


    // 비밀번호를 암호화 및 검증을 담당하는 객체
    @Bean
    public PasswordEncoder passwordEncoder()    {
        return new BCryptPasswordEncoder();
    }
}
