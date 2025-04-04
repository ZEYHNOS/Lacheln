package aba3.lucid.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // AuthenticationManager에서 반환된 Authentication의 정보를 이용하여 ContextHolder에 정보를 전달하는 로직을 수행
        CustomAuthenticationToken authRequest = (CustomAuthenticationToken) authentication;
        String username = authRequest.getName();
        String password = (String) authRequest.getCredentials();
        String loginType = ((CustomAuthenticationToken) authentication).getLoginType();
        Collection<GrantedAuthority> authorities = authRequest.getAuthorities();

        // ContextHolder에 저장할 정보를 구성
        CustomAuthenticationToken authenticationToken = null;
        CustomUserDetails userDetails;
        Long companyId;
        String userId;

        // 요청한 클라이언트의 권한(ROLE)을 확인 후 알맞은 세션 정보를 생성하여 ContextHolder에 저장하는 로직을 수행
        if("COMPANY".equals(loginType)){ // 업체일 경우의 로직
            userDetails = userDetailsService.loadUserByUsername(username);
            companyId = userDetails.getCompanyId();
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new RuntimeException("Invalid Credentials");
            }
            authenticationToken = new CustomAuthenticationToken(
                    userDetails.getUsername(),
                    null,
                    authorities,
                    userDetails.getRole(),
                    companyId  // 로그인 타입에 따라 적절한 ID를 전달
            );
        } else { // 소비자일 경우의 로직
            userDetails = userDetailsService.loadUserByUsername(username);
            userId = userDetails.getUserId();
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new RuntimeException("Invalid Credentials");
            }
            authenticationToken = new CustomAuthenticationToken(
                    userDetails.getUsername(),
                    null,
                    authorities,
                    userDetails.getRole(),
                    userId // 로그인 타입에 따라 적절한 ID를 전달
            );
        }
        return authenticationToken;
    }

    // 해당 메서드는 요청된 authentication에 대해 인증 로직을 수행할것인지 결정하는 메서드임
    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
