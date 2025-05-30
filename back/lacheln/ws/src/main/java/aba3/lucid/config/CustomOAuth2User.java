package aba3.lucid.config;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    // OAuth(소셜)로그인을 진행하는 유저의 정보를 저장하는 커스텀 클래스
    private String id;
    private String email;
    private String nickname;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("id", id, "email", email, "nickname", nickname);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getName() {
        return id;
    }
}
