package aba3.lucid.common.auth;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String loginType; // 추가 인자
    private final Long companyId; // 업체 PK
    private final String userId; // 유저 PK

    public CustomAuthenticationToken(String username,
                                     String password,
                                     Collection<GrantedAuthority> authorities,
                                     String loginType,
                                     String userId) {
        super(username, password, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = userId;
    }

    public CustomAuthenticationToken(String username,
                                     String password,
                                     Collection<GrantedAuthority> authorities,
                                     String loginType,
                                     Long companyId) {
        super(username, password, authorities);
        this.loginType = loginType;
        this.companyId = companyId;
        this.userId = null;
    }

    public CustomAuthenticationToken(Object principal,
                                     Object credentials,
                                     String loginType,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = null;
    }

    public CustomAuthenticationToken(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginType = null;
        this.companyId = null;
        this.userId = null;
    }
}
