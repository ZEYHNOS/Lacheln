package aba3.lucid.jwtconfig;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "cp_id", nullable = false, unique = true)
    private Long cpId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public CompanyRefreshToken(Long cpId, String refreshToken) {
        this.cpId = cpId;
        this.refreshToken = refreshToken;
    }

    public CompanyRefreshToken update(String newRefreshToken)    {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
