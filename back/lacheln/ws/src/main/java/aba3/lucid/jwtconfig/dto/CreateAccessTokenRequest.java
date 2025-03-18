package aba3.lucid.jwtconfig.dto;

import lombok.Getter;

@Getter
public class CreateAccessTokenRequest {
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
