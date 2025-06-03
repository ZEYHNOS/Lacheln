package aba3.lucid.handler;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.*;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.user.service.UserService;
import aba3.lucid.service.AuthService;
import com.fasterxml.uuid.Generators;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    // 소셜 로그인 성공 핸들러
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // ClientRegistration을 통해 client-name 확인
        String clientName = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String requestEmail = "";
        String requestName = "";
        String requestId = "";
        String social = "";

        // 사용자가 어느 플랫폼을 통해 로그인을 요청했는지 확인 후 플랫폼에 알맞은 로직을 수행
        if(clientName.equals("google")) {
            requestId = oAuth2User.getAttribute("sub");
            requestEmail = oAuth2User.getAttribute("email");
            requestName = oAuth2User.getAttribute("name");
            social = "G";
        } else if(clientName.equals("kakao")) {
            Map<String, Object> account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
            requestId = oAuth2User.getAttributes().get("id").toString();
            requestEmail = account.get("email").toString();
            requestName = properties.get("nickname").toString();
            social = "K";
        }

        // 해당하는 유저가 신규 유저일 경우 DB에 정보를 INSERT 진행
        if(usersRepository.findByUserEmail(requestEmail).isEmpty()) {
            saveOAuth2User(requestEmail, requestName, requestId, social);
        }

        // 토큰 발급 로직
        Map<String, ResponseCookie> cookies = authService.login(requestEmail, "USER");

        for (ResponseCookie cookie : cookies.values()) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        response.sendRedirect("http://localhost:3000");

    }

    // 정보 저장하는 로직
    public void saveOAuth2User(String email, String name, String id, String platform) {
        String nickName = platform+"_"+id;
        SocialEnum social = platform.equals("K") ? SocialEnum.K : SocialEnum.G;
        UsersEntity usersEntity = UsersEntity.builder()
                .userId(Generators.timeBasedGenerator().generate().toString())
                .userEmail(email)
                .userName(name)
                .userCountry(CountryEnum.KR)
                .userLanguage(LanguageEnum.KOR)
                .userCurrency(CurrencyEnum.USD)
                .userName(name)
                .userNickName(nickName)
                .userSocial(social)
                .userProfile("default.jpg")
                .userTier(TierEnum.AMATEUR)
                .userAdsNotification(NotificationEnum.N)
                .userAccountStatus(AccountStatusEnum.ACTIVE)
                .userJoinDate(LocalDate.now())
                .userModifyDate(LocalDate.now())
                .userAccessTime(LocalDateTime.now())
                .userGender(GenderEnum.U)
                .userMileage(new BigInteger("0"))
                .userRole("USER")
                .build();
        usersRepository.save(usersEntity);
    }
}
