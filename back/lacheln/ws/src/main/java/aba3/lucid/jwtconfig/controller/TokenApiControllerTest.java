package aba3.lucid.jwtconfig.controller;

import aba3.lucid.LachelnWebServer;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.*;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.jwtconfig.JwtFactory;
import aba3.lucid.jwtconfig.JwtProperties;
import aba3.lucid.jwtconfig.UserRefreshToken;
import aba3.lucid.jwtconfig.dto.CreateAccessTokenRequest;
import aba3.lucid.jwtconfig.repository.UserRefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LachelnWebServer.class)
//@AutoConfigureMockMvc
public class TokenApiControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext context;

//    @BeforeEach
//    public void mockMvcSetUp() {
//        usersRepository.deleteAll();
//    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    public void createNewUserAccessToken() throws Exception {
        // given : 테스트 유저를 생성하고, jjwt 라이브러리를 이용해 리프레시 토큰을 마들어 데이터베이스에 저장한다.
        //         토큰 생성 API의 요청 본문에 리프레시 토큰을 포함하여 요청 객체를 생성한다.
        final String url = "/api/token";

        UsersEntity testUser = usersRepository.save(UsersEntity.builder()
                .userId("UUD")
                .userEmail("test@gmail.com")
                .userPassword("12345678")
                .userLanguage(LanguageEnum.KOR)
                .userCurrency(CurrencyEnum.KRW)
                .userName("Cholsu")
                .userNickName("KingWangJang")
                .userBirthday(LocalDate.now())
                .userSocial(SocialEnum.G)
                .userProfile("default.png")
                .userTier(TierEnum.ADMIN)
                .userAdsNotification(NotificationEnum.Y)
                .userAccountStatus(AccountStatusEnum.ACTIVE)
                .userJoinDate(LocalDate.now())
                .userModifyDate(LocalDate.now())
                .userAccessTime(LocalDateTime.now())
                .userGender(GenderEnum.M)
                .mileage(new BigInteger("2379274"))
                .userRole("USER")
                .build());

        String refreshToken = JwtFactory.builder()
                ._claims(Map.of("id", testUser.getUserId()))
                .build()
                .createToken(jwtProperties);

        userRefreshTokenRepository.save(new UserRefreshToken(testUser.getUserId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);
        System.out.println(requestBody);
        // when : 토큰 추가 API에 요청을 보낸다.
        //        이때 요청 타입은 JSON이고, given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보낸다.
//        ResultActions resultActions = mockMvc
//                .perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(requestBody));
////
//        // then : 응답 코드가 201 Created인지 확인하고 응답으로 온 액세스 토큰이 비어 있지 않은지 확인한다.
//        resultActions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
