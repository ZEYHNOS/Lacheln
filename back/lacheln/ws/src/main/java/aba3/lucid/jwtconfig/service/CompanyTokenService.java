package aba3.lucid.jwtconfig.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.token.TokenCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.service.UserService;
import aba3.lucid.jwtconfig.JwtProvider;
import aba3.lucid.securityconfig.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CompanyTokenService {
    private final JwtProvider jwtProvider;
    private final CompanyRefreshTokenService companyRefreshTokenService;
    private final CompanyService companyService;

    public String createNewAccessToken(String refreshToken) {
        if(!jwtProvider.validToken(refreshToken)) {
            throw new ApiException(TokenCode.TOKEN_NOT_FOUND, "token is not found");
        }
        Long companyId = companyRefreshTokenService.findByRefreshToken(refreshToken).getCpId();
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        CustomUserDetails customUser = CustomUserDetails.builder()
                .companyId(company.getCpId())
                .userEmail(company.getCpEmail())
                .userType(company.getCpRole())
                .username(company.getCpName())
                .build();

        return jwtProvider.generateToken(customUser, Duration.ofHours(2));
    }
}
