package aba3.lucid.common.auth;

import aba3.lucid.domain.user.enums.TierEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
@ToString
public class  CustomUserDetails implements UserDetails {

    // UserDetails 구현체를 상속받아 현재 프로젝트에 필요한 정보들로 재구성한 클래스
    private String email;
    private String password;
    private String role;
    private String userId;
    private TierEnum tier;
    private Long companyId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
