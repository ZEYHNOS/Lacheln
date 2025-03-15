package aba3.lucid.JwtConfig;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    // 해당 패키지에 있는 applicaion.properties에 접근하여 jwt에 설정한 값들을 변수로 저장
    private String issuer;
    private String secretKey;
}
