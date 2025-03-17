package aba3.lucid.JwtConfig;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@SpringBootTest
public class JwtFactory {
    private String subject = "test";
    private long subject2 = 1231724;
    private Date issuedAt = new Date();
    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = new HashMap <>();

    @Builder
    public JwtFactory(String _subject, Date _issuedAt, Date _expiration, Map<String, Object> _claims)   {
        this.subject = _subject != null ? _subject : this.subject;
        this.issuedAt = _issuedAt != null ? _issuedAt : this.issuedAt;
        this.expiration = _expiration != null ? _expiration : this.expiration;
        this.claims = _claims != null ? _claims : this.claims;
    }

    public static JwtFactory withDefaultValues()    {
        return JwtFactory.builder().build();
    }

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}
