package aba3.lucid.jwtconfig.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.CompanyCode;
import aba3.lucid.jwtconfig.CompanyRefreshToken;
import aba3.lucid.jwtconfig.repository.CompanyRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyRefreshTokenService {
    private final CompanyRefreshTokenRepository companyRefreshTokenRepository;

    // 새로 만들어 전달받은 RefreshToken으로 RefreshToken 객체를 검색해서 전달
    public CompanyRefreshToken findByRefreshToken(String refreshToken) {
        return companyRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiException(CompanyCode.MEMBER_NOT_FOUND, "Refresh Token is not found"));
    }
}
