package aba3.lucid.handler;

import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.*;
import aba3.lucid.domain.user.service.UserService;
import com.fasterxml.uuid.Generators;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // ClientRegistration을 통해 client-name 확인
        String clientName = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String requestEmail = oAuth2User.getAttribute("email");
        String requestName = oAuth2User.getAttribute("name");
        String requestId = "";
        String social = "";
        if(clientName.equals("google")) {
            requestId = oAuth2User.getAttribute("sub");
            social = "G";
        } else if(clientName.equals("kakao")) {
            requestId = oAuth2User.getAttribute("id")+"";
            social = "K";
        }

        if(userService.findById(requestEmail).isEmpty()) {
            saveOAuth2User(requestEmail, requestName, requestId, social);
        }


        System.out.println("로그인 성공 : " + oAuth2User.getAttributes());
    }

    public void saveOAuth2User(String email, String name, String id, String platform) {
        String nickName = platform+"_"+id;
        UsersEntity usersEntity = UsersEntity.builder()
                .userId(Generators.timeBasedGenerator().generate().toString())
                .userEmail(email)
                .userName(name)
                .userCountry(CountryEnum.KR)
                .userLanguage(LanguageEnum.KOR)
                .userCurrency(CurrencyEnum.USD)
                .userName(name)
                .userNickName(nickName)
                .userSocial(SocialEnum.G)
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
        if(platform.equals("G")) {
            userService.saveOAuthUser(usersEntity);
        } else if(platform.equals("K")) {
            userService.saveOAuthUser(usersEntity);
        }
    }
}
