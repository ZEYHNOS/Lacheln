package aba3.lucid.securityconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 사용자와 업체의 정보 2가지를 전부 다룰 제공자
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 사용자 정보 및 업체 정보를 선언
    private final CustomUserDetailsService userDetailsService;
    private final CustomCompanyDetailsService companyDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 인증 진행
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException    {
        // 사용자, 업체 이름과 비밀번호 저장
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails;

        // 사용자의 정보 가져오기
        // 사용자의 정보를 가져올 수 없다면 업체임으로 업체 정보 가져오기
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch(UsernameNotFoundException e)    {
            userDetails = companyDetailsService.loadUserByUsername(username);
        }

        // 사용자든 업체든 가져온 정보로 암호 비교
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        // 모든 과정이 끝나면 토큰 발급
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    // 위의 authenticate 메서드로 요청을 처리 할 수 있는지 확인하는 메서드(True반환시 authenticate 메서드 실행)
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
