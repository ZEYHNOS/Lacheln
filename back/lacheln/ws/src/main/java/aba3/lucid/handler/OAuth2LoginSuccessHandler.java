package aba3.lucid.handler;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.*;
import aba3.lucid.domain.user.service.UserService;
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

    private final UserService userService;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2LoginSuccessHandler called");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // ClientRegistration을 통해 client-name 확인
        String clientName = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String requestEmail = "";
        String requestName = "";
        String requestId = "";
        String social = "";

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

        if(userService.findByEmail(requestEmail).isEmpty()) {
            saveOAuth2User(requestEmail, requestName, requestId, social);
        }

        System.out.println("==== 로그인 요청 정보 시작 ====");
        System.out.println("requestEmail : " + requestEmail);
        System.out.println("requestName : " + requestName);
        System.out.println("requestId : " + requestId);
        System.out.println("requestSocial : " + social);
        System.out.println("==== 로그인 요청 정보 끝 ====");

        // 토큰 발급 로직
        Map<String, ResponseCookie> cookies = authService.login(requestEmail, "USER");

        for (ResponseCookie cookie : cookies.values()) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        System.out.println("OAuth Authentication Successful : " + oAuth2User.getAttributes());

        response.sendRedirect("http://localhost:3000");
    }

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
        userService.saveByUser(usersEntity);
    }
}
