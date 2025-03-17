package aba3.lucid.jwtconfig.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.UserCode;
import aba3.lucid.jwtconfig.UserRefreshToken;
import aba3.lucid.jwtconfig.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRefreshTokenService {
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // 새로 만들어 전달받은 RefreshToken으로 RefreshToken 객체를 검색해서 전달
    public UserRefreshToken findByRefreshToken(String refreshToken) {
        return userRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiException(UserCode.USER_NOT_FOUND, "Refresh Token is not found"));
    }
}
