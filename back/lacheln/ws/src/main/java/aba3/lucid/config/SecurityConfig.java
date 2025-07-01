    package aba3.lucid.config;

    import aba3.lucid.domain.company.repository.CompanyRepository;
    import aba3.lucid.domain.user.repository.UsersRepository;
    import aba3.lucid.filter.CustomUsernamePasswordAuthenticationFilter;
    import aba3.lucid.handler.OAuth2LoginFailureHandler;
    import aba3.lucid.handler.OAuth2LoginSuccessHandler;
    import aba3.lucid.jwt.JwtAuthenticationFilter;
    import aba3.lucid.service.AuthService;
    import aba3.lucid.service.OAuth2UserService;
    import com.nimbusds.jose.crypto.impl.PBKDF2;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;

    import java.util.Arrays;
    import java.util.List;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final OAuth2UserService Oauth2UserService;
        private final OAuth2LoginSuccessHandler Oauth2LoginSuccessHandler;
        private final OAuth2LoginFailureHandler Oauth2LoginFailureHandler;

        //TODO ROLE(소비자, 업체, 일반사용자)에 따라 접근 가능한 URL들을 저장하는 리스트
        private final String[] permitAlls = { "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/email/send", "/email/verify", "/static/**" };
        private final String[] roleUser = { "/user/**", "/board/**" };
        private final String[] roleCompany = { "/company/**", "/product/**" };
        
        //TODO TIER(Amateur, SEMIPRO, PRO, WORLDCLASS, CHALLENGER)에 따라 접근 가능한 URL들을 저장하는 리스트
        private final String[] AMATEUR = {};
        private final String[] SEMIPRO = {};
        private final String[] PRO = {};
        private final String[] WORLDCLASS = {};
        private final String[] CHALLENGER = {};

        // Http 요청을 가로채어 인증진행
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, AuthService authService, UsersRepository usersRepository, CompanyRepository companyRepository) throws Exception {
            CustomUsernamePasswordAuthenticationFilter customFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager, companyRepository, usersRepository, authService);
            http
                    .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화(세션 사용시에만 활성화 우리는 토큰 사용으로 필요없음)
                    .cors(cors -> cors.configurationSource(request -> { // TODO AbstractHttpConfigurer::disable
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://52.79.195.13:3000", "http://lacheln.p-e.kr:3000", "http://localhost:3000"));
                        config.setAllowCredentials(true);
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(List.of("*"));
                        return config;
                    })) // CORS 설정진행
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(permitAlls).permitAll()
                            .requestMatchers(roleUser).permitAll()                  //TODO .hasRole("USER") 추가예정
                            .requestMatchers(roleCompany).permitAll()               //TODO .hasRole("COMPANY") 추가예정
                            .requestMatchers(AMATEUR).hasRole("TIER_AMATEUR")       //TODO Tier에 따라 접근 가능한 URL주소 등록
                            .requestMatchers(SEMIPRO).hasRole("TIER_SEMIPRO")       //TODO Tier에 따라 접근 가능한 URL주소 등록
                            .requestMatchers(PRO).hasRole("TIER_PRO")               //TODO Tier에 따라 접근 가능한 URL주소 등록
                            .requestMatchers(WORLDCLASS).hasRole("TIER_WORLDCLASS") //TODO Tier에 따라 접근 가능한 URL주소 등록
                            .requestMatchers(CHALLENGER).hasRole("TIER_CHALLENGER") //TODO Tier에 따라 접근 가능한 URL주소 등록
                            .anyRequest().permitAll()
                    ) // ROLE에 따른 접근 권한 설정
                    .formLogin(form -> form.loginProcessingUrl("/login"))
                    .oauth2Login((oauth2) -> oauth2
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                            .userService(Oauth2UserService))
                            .successHandler(Oauth2LoginSuccessHandler)
                            .failureHandler(Oauth2LoginFailureHandler)) // OAuth로그인 진행 설정
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class) // 사용자 인증에 대한 로직실행
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 토큰에 대한 인증 실행
            return http.build();
        }

        // authencticationManager Bean주입
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        // 암호화 모듈 Bean 주입
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }