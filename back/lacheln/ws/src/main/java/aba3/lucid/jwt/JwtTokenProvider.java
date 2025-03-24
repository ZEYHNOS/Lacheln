package aba3.lucid.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    // 환경 변수에 있는 Secret Key값 가져오기
    @Value("${jwt.SECRET_KEY}")
    private String secretKey;

    // DI
    private final UserDetailsService userDetailsService;

    // Access 토큰 생성
    public String createAccessToken(String userEmail, String role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", "ROLE_" + role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + Duration.ofDays(1).toMillis()); // 1시간 유효

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(String userEmail, String role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", "ROLE_" + role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + Duration.ofDays(7).toMillis()); // 1일 유효

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        System.out.println("userDetails = " + userDetails.getUsername() + " " + userDetails.getPassword() + " " + userDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 유저 이메일 추출
    public String getUserEmail(String token) {
        System.out.println("getUserEmail = " + Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 유저 권한 추출
    public String getUserRole(String token) {
        Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String role = claim.get("role").toString();
        System.out.println("role = " + role);
        return role;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.out.println("!claims.getBody().getExpiration().before(new Date()) = " + !claims.getBody().getExpiration().before(new Date()));
            return !claims.getBody().getExpiration().before(new Date()); // 혹시 모르니 찾아볼 것 -->  ! (추가여부)
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("validateToken False");
            return false;
        }
    }
 
    // 토큰 만료시간 검증
    public boolean isExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.out.println("claims.getBody() = " + claims.getBody().getExpiration().before(new Date()));
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("혹시 여기로 오니?");
            return true;
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
