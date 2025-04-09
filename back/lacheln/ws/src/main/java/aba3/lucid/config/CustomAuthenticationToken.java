package aba3.lucid.config;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    // 해당하는 클래스는 ContextHolder에 저장될 정보들임
    private final String loginType; // 추가 인자
    private final Long companyId; // 업체 PK
    private final String userId; // 유저 PK

    // 소비자와 업체의 ID타입이 다르기 때문에 각 타입에 맞게 Overload를 진행
    public CustomAuthenticationToken(String username, String password, Collection<? extends GrantedAuthority> authorities, String loginType, String userId) {
        super(username, password, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = userId;
    }

    public CustomAuthenticationToken(String companyName, String password, Collection<? extends GrantedAuthority> authorities, String loginType, Long companyId) {
        super(companyName, password, authorities);
        this.loginType = loginType;
        this.companyId = companyId;
        this.userId = null;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, String loginType, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.loginType = loginType;
        this.companyId = null;
        this.userId = null;
    }

    public CustomAuthenticationToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginType = null;
        this.companyId = null;
        this.userId = null;
    }
}
