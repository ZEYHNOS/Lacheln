package aba3.lucid.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthenticationToken customAuth = (CustomAuthenticationToken) authentication;
        String userEmail = customAuth.getName();
        String password = customAuth.getCredentials().toString();
        String loginType = customAuth.getLoginType(); // 추가된 인자 사용

        // 로그인 타입에 따라 다른 로직 수행 가능
        UserDetails userDetails;
        if ("USER".equals(loginType)) {
            userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        } else if ("COMPANY".equals(loginType)) {
            userDetails = customUserDetailsService.loadUserByUsername(userEmail); // 업체 로그인 처리 (예시)
        } else {
            throw new BadCredentialsException("Invalid login type");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        // 인증 성공 시 새로운 CustomAuthenticationToken 반환
        return new CustomAuthenticationToken(userDetails, password, loginType, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
