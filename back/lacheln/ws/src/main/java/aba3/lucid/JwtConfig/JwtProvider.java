package aba3.lucid.JwtConfig;

import aba3.lucid.SecurityConfig.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;


import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(CustomUserDetails user, Duration expiredAt)  {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // Jwt 토큰 생성 메서드
    private String makeToken(Date expiry, CustomUserDetails user)   {
        Date now = new Date();
        String getId = user.getUserType().equals("USER") ? user.getUserId() : String.valueOf(user.getCompanyId());
        String getType = user.getUserType();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 타입 설정 : Jwt
                .setIssuer(jwtProperties.getIssuer()) // Issuer 설정
                .setIssuedAt(now) // Issue 시점
                .setExpiration(expiry) // 만료시점
                .setSubject(getId) // 내용.. 일단 해당 유저 ID로함
                .claim("type", getType)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 : HS256 방식으로 암호화
                .compact();
    }

    // Jwt 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화 진행
                    .parseClaimsJws(token);
            return true;
        } catch(Exception e) { 
            // 만약 복호화 과정에 문제가 있다면 유효하지 않은 토큰으로 Exception 발생
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 Get하는 메서드
    public Authentication getAuthentication(String token)   {
        Claims claims = getClaims(token);
        String role = claims.get("role", String.class);
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));

        if(role.equals("USER")) {
            String userId = claims.get("userId", String.class);
            return new UsernamePasswordAuthenticationToken(CustomUserDetails.builder().userId(userId).password("").userType("USER").companyId(0),token, authorities);
        }


        long companyId = Long.parseLong(claims.get("subject", String.class));
        return new UsernamePasswordAuthenticationToken(CustomUserDetails.builder().userId("").password("").userType("COMPANY").companyId(companyId),token, authorities);
    }

    public long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token)  {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
