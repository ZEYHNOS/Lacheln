package aba3.lucid.service;

import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.jwt.JwtTokenProvider;
import aba3.lucid.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UsersRepository userRepository;
    private final CompanyRepository companyRepository;

    // 로그인 성공 후 토큰을 발급하는 로직
    public Map<String, ResponseCookie> login(String userEmail, String role)  {
        // Access, Refresh 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(userEmail, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail, role);
        Map<String, ResponseCookie> responseCookies = new HashMap<>();

        // RefreshToken은 Redis에 저장
        refreshTokenService.saveRefreshToken(userEmail, refreshToken);

        // 쿠키에 각 토큰을 저장
        ResponseCookie accessCookie = ResponseCookie.from("AccessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(Duration.ofDays(10))
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(Duration.ofDays(10))
                .build();

        responseCookies.put("AccessToken", accessCookie);
        responseCookies.put("RefreshToken", refreshCookie);

        return responseCookies;
    }

    // 로그아웃 후 진행되는 토큰 제거 로직
    public Map<String, ResponseCookie> logout(String refreshToken) {
        Map<String, ResponseCookie> responseCookies = new HashMap<>();
        
        // 토큰에 저장된 이메일 정보 추출
        String userEmail = jwtTokenProvider.getUserEmail(refreshToken);

        // 추출한 정보를 기반으로 Redis에 저장되어있는 RefreshToken 정보 제거
        refreshTokenService.deleteRefreshToken(userEmail);

        // 유효기간이 0(바로 사라짐)인 Access,RefreshToken을 저장한 쿠키생성
        ResponseCookie accessCookie = ResponseCookie.from("AccessToken", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 개발단계에서는 None 배포 시 strict
                .path("/")
                .maxAge(0)
                .build();

        responseCookies.put("AccessToken", accessCookie);
        responseCookies.put("RefreshToken", refreshCookie);

        return responseCookies;
    }

    // 토큰 정보를 기반으로 유저 정보(ROLE)을 추출하여 소비자, 업체를 구분하여 알맞게 정보를 제거하는 로직
    public void withdrawUsers(String accessToken, Authentication authentication) {
        String role = jwtTokenProvider.getUserRole(accessToken);
        if(role.equals("ROLE_USER"))    {
            deleteUser(authentication.getPrincipal().toString());
            log.info("유저정보 제거, {}");
        } else if(role.equals("ROLE_COMPANY"))  {
            deleteCompany(Long.parseLong(authentication.getPrincipal().toString()));
            log.info("업체정보 제거, {}");
        }
    }

    // 각 Entity의 Respository에 접근하여 DB의 정보를 DROP하는 로직
    public void deleteUser(String userId) { userRepository.deleteById(userId); }
    public void deleteCompany(Long companyId) { companyRepository.deleteById(companyId); }

    // Access 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
        // RefreshToken 조회를 통해 AccessToken을 재발급 해야할 유저 정보 추출
        String accessEmail = jwtTokenProvider.getUserEmail(refreshToken);
        String role = jwtTokenProvider.getUserRole(refreshToken);

        // Redis에서 Refresh Token을 추출
        String savedRefreshToken = refreshTokenService.getRefreshToken(accessEmail);

        // RefreshToken이 존재할 경우 새로운 AccessToken 생성
        if (savedRefreshToken != null) {
            return jwtTokenProvider.createAccessToken(accessEmail, role);
        } else {
            throw new RuntimeException("Invalid or expired refresh token");
        }
    }

    public String getUserNickName(String userEmail) {
        UsersEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.GONE, "해당하는 유저가 없습니다."));
        return user.getUserNickName();
    }

    public String getCompanyName(String cpEmail) {
        CompanyEntity cp = companyRepository.findByCpEmail(cpEmail)
                .orElseThrow(() -> new ApiException(ErrorCode.GONE, "해당하는 업체가 존재하지 않습니다."));
        return cp.getCpName();
    }
}
