package aba3.lucid.common.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Slf4j
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String loginType; // "USER" 또는 "COMPANY"
    private final Long companyId;
    private final String userId;

    // 유저 인증 생성자
    public CustomAuthenticationToken(String username,
                                     String password,
                                     Collection<? extends GrantedAuthority> authorities,
                                     String loginType,
                                     String userId) {
        super(username, password, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = userId;
        log.info("user authenticated");
    }

    // 업체 인증 생성자
    public CustomAuthenticationToken(String username,
                                     String password,
                                     Collection<? extends GrantedAuthority> authorities,
                                     String loginType,
                                     Long companyId) {
        super(username, password, authorities);
        this.loginType = loginType;
        this.companyId = companyId;
        this.userId = null;
        log.info("company authenticated");
    }

    // 인증 이후에 사용할 생성자 (Principal, Credentials 등)
    public CustomAuthenticationToken(Object principal,
                                     Object credentials,
                                     String loginType,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = null;
    }

    // 기타 용도
    public CustomAuthenticationToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginType = null;
        this.companyId = null;
        this.userId = null;
    }

    @Override
    public Object getPrincipal() {
        return super.getPrincipal(); // CustomUserDetails 반환 가능
    }
}
