package aba3.lucid.jwtconfig.controller;

import aba3.lucid.jwtconfig.dto.CreateAccessTokenRequest;
import aba3.lucid.jwtconfig.dto.CreateAccessTokenResponse;
import aba3.lucid.jwtconfig.service.CompanyTokenService;
import aba3.lucid.jwtconfig.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final CompanyTokenService companyTokenService;
    private final UserTokenService userTokenService;

    @PostMapping("/user/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewUserAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = userTokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    @PostMapping("/company/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewCompanyAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = companyTokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }
}
