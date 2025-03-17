package aba3.lucid.jwtconfig.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.token.TokenCode;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.service.UserService;
import aba3.lucid.jwtconfig.JwtProvider;
import aba3.lucid.securityconfig.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final JwtProvider jwtProvider;
    private final UserRefreshTokenService userRefreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if(!jwtProvider.validToken(refreshToken)) {
            throw new ApiException(TokenCode.TOKEN_NOT_FOUND, "Token is not valid");
        }
        String userId = userRefreshTokenService.findByRefreshToken(refreshToken).getUserId();
        UsersEntity user = userService.findById(userId);
        CustomUserDetails customUser = CustomUserDetails.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userType(user.getUserRole())
                .username(user.getUserName())
                .build();

        return jwtProvider.generateToken(customUser, Duration.ofHours(2));
    }
}
