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
                .csrf(csrf -> csrf.disable()) // REST API를 이용한 서버임으로 csrf 보호 비활성화
                .authorizeHttpRequests(auth -> auth // 특정 패턴에 해당하는 url이 요청될 경우 사용자의 권한을 확인하여 인증 여부 및 인증 진행
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/company/**").hasRole("COMPANY")
                        .requestMatchers("/api/**").permitAll() // 해당하는 요청은 모든 사용자들이 접근 할 수 있도록 허가(추가 예정)
                        .anyRequest().authenticated() //  그 외 모든 요청에 대해서는 인증을 마친 사용자만 접근 할 수 있도록 설정
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // 인증 프로세스를 담당하는 Bean 생성(사용자, 업체 각각 암호화된 상태로 비교)
    @Bean
    public AuthenticationManager authenticationManager() {
        // 사용자 인증을 처리하는 인스턴스 생성
        DaoAuthenticationProvider userAuthProvider = new DaoAuthenticationProvider();
        // 해당하는 인스턴스에 사용자의 정보를 SET
        userAuthProvider.setUserDetailsService(userDetailsService);
        // 사용자의 비밀번호를 암호화시킴
        userAuthProvider.setPasswordEncoder(passwordEncoder());

        // 업체의 인증을 처리하는 인스턴스 생성
        DaoAuthenticationProvider companyAuthProvider = new DaoAuthenticationProvider();
        // 해당하는 인스턴스에 업체 정보를 SET
        companyAuthProvider.setUserDetailsService(companyDetailsService);
        // 업체의 비밀번호를 암호화 시킴
        companyAuthProvider.setPasswordEncoder(passwordEncoder());

        // 해당하는 인스턴스에 사용자와 업체 정보를 리스트에 저장하여 다중 인증 클래스 사용
        return new ProviderManager(List.of(userAuthProvider, companyAuthProvider));
    }

    // 비밀번호를 암호화 및 검증을 담당하는 객체
    @Bean
    public PasswordEncoder passwordEncoder()    {
        return new BCryptPasswordEncoder();
    }
}
