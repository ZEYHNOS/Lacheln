package aba3.lucid.jwt;

import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.TierEnum;
import aba3.lucid.service.CustomUserDetailsService;
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
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);
        log.info("tier : {}", "" + userDetails.getTier());
        claims.put("tier", userDetails.getTier());
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
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role);
        claims.put("tier", userDetails.getTier());
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
        TierEnum tier = userDetails.getTier();
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role),
                new SimpleGrantedAuthority("ROLE_TIER_" + tier)
        );
        customAuthenticationToken = new CustomAuthenticationToken(userDetails, "", role, tier, authorities);
        return customAuthenticationToken;
    }

    public String getUserId(String token)   {
        String userEmail = getUserEmail(token);
        return customUserDetailsService.getUserIdByEmail(userEmail);
    }

    // 토큰에서 유저 이메일 추출
    public String getUserEmail(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT에서 사용자 이메일 추출 실패: {}", e.getMessage());
            return null;
        } catch (JwtException e) {
            log.error("잘못된 JWT에서 사용자 이메일 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // 토큰에서 유저 권한 추출
    public String getUserRole(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("role").toString();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT에서 권한(role) 추출 실패: {}", e.getMessage());
            return null; // 혹은 Optional<String> 사용도 가능
        } catch (JwtException e) {
            log.error("잘못된 JWT에서 권한(role) 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    // 토큰이 유효한지 검사하는 메소드
    public boolean isValid(String token) {
        log.info("JWT VALUE : {}", token);
        try {
            // 토큰에서 만료 일자와 서명을 검증
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);  // 유효한 토큰이면 예외가 발생하지 않음
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration.after(new Date());
        } catch (ExpiredJwtException e) {
            log.warn("JWT 토큰 만료: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
            return false;
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
