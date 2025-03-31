package aba3.lucid.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String loginType; // 추가 인자

    public CustomAuthenticationToken(String username, String password, String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, String loginType, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
    }
}
