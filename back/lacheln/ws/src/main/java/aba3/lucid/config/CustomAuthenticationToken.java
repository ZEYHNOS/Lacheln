package aba3.lucid.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

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

    public String getLoginType() {
        return loginType;
    }
}
