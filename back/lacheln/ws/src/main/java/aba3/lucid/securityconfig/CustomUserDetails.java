package aba3.lucid.securityconfig;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class CustomUserDetails  implements UserDetails {
    private final String userEmail;
    private final String username;
    private final String password;
    private final String userType;
    private final String userId;
    private final long companyId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if(userType.equals("USER")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if(userType.equals("COMPANY")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_COMPANY"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
