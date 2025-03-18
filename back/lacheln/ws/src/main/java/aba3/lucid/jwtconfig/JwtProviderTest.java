package aba3.lucid.jwtconfig;

import aba3.lucid.LachelnWebServer;
import aba3.lucid.securityconfig.CustomUserDetails;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.company.enums.CompanyStatus;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = LachelnWebServer.class)
public class JwtProviderTest    {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtProperties jwtProperties;


    // 토큰 검증 테스트
    @Test
    @Transactional
    @DisplayName("generateToken() : 유저 정보와 만료 기간을 전달하여 토큰을 생성한다.")
    void generateTokens() {
        CustomUserDetails customUser;
//        // 테스트 전용 유저 정보 생성
//        UsersEntity user = usersRepository.save(UsersEntity.builder()
//                .userId("UUID")
//                .userEmail("test@gmail.com")
//                .userPassword("12345678")
//                .userLanguage(LanguageEnum.KOR)
//                .userCurrency(CurrencyEnum.KRW)
//                .userName("Cholsu")
//                .userNickName("KingWangJang")
//                .userBirthday(LocalDate.now())
//                .userSocial(SocialEnum.G)
//                .userProfile("default.png")
//                .userTier(TierEnum.ADMIN)
//                .userAdsNotification(NotificationEnum.Y)
//                .userAccountStatus(AccountStatusEnum.ACTIVE)
//                .userJoinDate(LocalDate.now())
//                .userModifyDate(LocalDate.now())
//                .userAccessTime(LocalDateTime.now())
//                .userGender(GenderEnum.M)
//                .mileage(new BigInteger("2379274"))
//                .userRole("USER")
//                .build());
//
//        customUser = CustomUserDetails.builder()
//                .userEmail(user.getUserEmail())
//                .password(user.getUserPassword())
//                .userType(user.getUserRole())
//                .userId(user.getUserId())
//                .build();

////      테스트 전용 업체 정보 생성
//        CompanyEntity company = companyRepository.save(CompanyEntity.builder()
//                .cpId(123)
//                .cpEmail("cpTest@gmail.com")
//                .cpPassword("87654321")
//                .cpName("ffff")
//                .cpRepName("손지훈")
//                .cpMainContact("010-3333-5555")
//                .cpAddress("대구광역시 북구 복현동 영진전문대학교 도서관 G03")
//                .cpPostalCode("55555")
//                .cpBnRegNo("123798")
//                .cpMos("16378563875")
//                .cpStatus(CompanyStatus.ACTIVATE)
//                .cpProfile("lacheln.png")
//                .cpExplain("1000년역사를 함께한 장인드레스 전문 컴퍼니")
//                .cpCategory(CompanyCategory.D)
//                .cpContact("031-0358-2134")
//                .cpFax("54321333")
//                .companyRole("COMPANY")
//                .build());

        CompanyEntity company = CompanyEntity.builder()
                .cpId(123)
                .cpEmail("cpTest@gmail.com")
                .cpPassword("87654321")
                .cpName("ffff")
                .cpRepName("손지훈")
                .cpMainContact("010-3333-5555")
                .cpAddress("대구광역시 북구 복현동 영진전문대학교 도서관 G03")
                .cpPostalCode("55555")
                .cpBnRegNo("123798")
                .cpMos("16378563875")
                .cpStatus(CompanyStatus.ACTIVATE)
                .cpProfile("lacheln.png")
                .cpExplain("1000년역사를 함께한 장인드레스 전문 컴퍼니")
                .cpCategory(CompanyCategory.D)
                .cpContact("031-0358-2134")
                .cpFax("54321333")
                .cpRole("COMPANY")
                .build();

        customUser = CustomUserDetails.builder()
                .userEmail(company.getCpEmail())
                .password(company.getCpPassword())
                .companyId(company.getCpId())
                .userType(company.getCpRole())
                .build();

        // JwtProvider를 통해 Token을 생성한다.
        String token = jwtProvider.generateToken(customUser, Duration.ofDays(14));

        String customUserId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class);

        // 사용자 아이디 비교
//        assertThat(customUserId).isEqualTo(customUser.getUserId());

        // 업체 아이디 비교
        assertThat(customUserId).isEqualTo(customUser.getCompanyId()+"");
    }

    // 만료된 토큰 검증 테스트
    @Test
    @DisplayName("vaildToken() : 만료된 토큰일 경우 유효성 검증 실패처리")
    void vaildToken()   {
        String token = JwtFactory.builder()
                ._expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = jwtProvider.validToken(token);

        assertThat(result).isFalse();
    }

    // 토큰의 정보를 이용하여 인증정보를 가져오는 테스트 코드
    @Test
    @DisplayName("getAuthentication() : 토큰을 기반으로 인증정보를 가져오는 테스트")
    void getAuthentication()    {
        String userEmail = "test@gmail.com";
        String token = JwtFactory.builder()
                ._subject(userEmail)
                ._role("USER")
                .build()
                .createToken(jwtProperties);

        Authentication authentication = jwtProvider.getAuthentication(token);
        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        System.out.println(customUser.getUserType());
        assertThat(customUser.getUserEmail()).isEqualTo(userEmail);
    }

    // 토큰의 정보를 이용하여 사용자 ID를 가져온다.
    @Test
    @DisplayName("getUserId() : 토큰 정보를 이용해 사용자 ID를 가져올 수 있다.")
    void getUserId()    {
        String userId = "test";
        String token = JwtFactory.builder()
                ._claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        String userIdByToken = jwtProvider.getUserId(token);
        System.out.println(userIdByToken + " " + userId);
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
