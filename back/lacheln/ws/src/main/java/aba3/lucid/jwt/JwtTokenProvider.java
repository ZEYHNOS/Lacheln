package aba3.lucid.jwt;

import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.config.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    // 환경 변수에 있는 Secret Key값 가져오기
    @Value("${jwt.SECRET_KEY}")
    private String secretKey;

    // DI
    private final CustomUserDetailsService customUserDetailsService;

    // Access 토큰 생성
    public String createAccessToken(String userEmail, String role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + Duration.ofDays(1).toMillis()); // 1시간 유효

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String userEmail, String role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + Duration.ofDays(7).toMillis()); // 1일 유효
        log.info("secretKey : {}", secretKey);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserEmail(token));
        CustomAuthenticationToken customAuthenticationToken;
        String role = userDetails.getRole(); // "USER" 또는 "COMPANY"일 경우
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );
        customAuthenticationToken = new CustomAuthenticationToken(userDetails, "", role, authorities);
        return customAuthenticationToken;
    }

    public String getUserId(String token)   {
        return customUserDetailsService.getUserIdByEmail(getUserEmail(token));
    }

    // 토큰에서 유저 이메일 추출
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 유저 권한 추출
    public String getUserRole(String token) {
        Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claim.get("role").toString();
    }

    // 토큰 만료시간 검증
    public boolean isExpired(Jws<Claims> claims) {
        try {
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 토큰이 유효한지 검사하는 메소드
    public boolean isValid(String token) {
        try {
            // 토큰에서 만료 일자와 서명을 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);  // 유효한 토큰이면 예외가 발생하지 않음

            return !isExpired(claimsJws); // 만료되었으면 false 반환
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;  // 예외 발생 시 유효하지 않은 토큰으로 간주
        }
    }

    // 헤더에서 JWT 추출
    public Map<String, String> resolveTokens(HttpServletRequest request) {
        Cookie[] bearerTokens = request.getCookies();
        Map<String, String> tokens = new HashMap<>();
        String accessToken = null;
        String refreshToken = null;

        if(bearerTokens != null){
            for(Cookie cookie : bearerTokens) {
                if(cookie.getName().equals("AccessToken")) {
                    accessToken = cookie.getValue();
                } else if(cookie.getName().equals("RefreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        tokens.put("AccessToken", accessToken);
        tokens.put("RefreshToken", refreshToken);

        return tokens;
    }
}
