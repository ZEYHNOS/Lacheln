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
        System.out.println("CustomAuthenticationProvider called");
        CustomAuthenticationToken authRequest = (CustomAuthenticationToken) authentication;
        String username = authRequest.getName();
        String password = (String) authRequest.getCredentials();
        String loginType = ((CustomAuthenticationToken) authentication).getLoginType();
        Collection<GrantedAuthority> authorities = authRequest.getAuthorities();
        CustomAuthenticationToken authenticationToken = null;
        CustomUserDetails userDetails;
        Long companyId;
        String userId;

        if("COMPANY".equals(loginType)){
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
        } else {
            userDetails = userDetailsService.loadUserByUsername(username);
            userId = userDetails.getUserId();
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                log.info("password, {}", password);
                log.info("userDetails.getPassword(), {}", userDetails.getPassword());
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

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
