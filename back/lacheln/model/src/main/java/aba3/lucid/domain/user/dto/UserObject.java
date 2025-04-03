package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.user.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserObject {

    private String userId; //소비자ID

    private CountryEnum userCountry; //국가명코드 ISO 3166-1 alpha-2

    private LanguageEnum userLanguage; //국가언어고유코드 ISO 639 alpha-3

    private CurrencyEnum userCurrency; //화폐단위 ISO 4217

    private String userEmail;

    private String userPassword; //암호화 할거라 길이 255

    private String userName; //외국 이름 포함

    private String userNickName; //닉네임

    private LocalDate userBirthday; //생년월일

    private SocialEnum userSocial; //로그인 수단 식별 로컬, 카카오, 구글, 애플(L, K, G, A)

    private String userPhone; //전화번호

    private String userProfile; //프로필URL 디폴트 이미지 있음

    private TierEnum userTier; //챌린저,월드클래스,프로페셔널,세미프로,아마추어,admin

    private NotificationEnum userAdsNotification; //알림 동의 여부 Y, N

    private AccountStatusEnum userAccountStatus; //활성화, 비활성화, 휴면, 정지

    private LocalDate userJoinDate; //가입일

    private LocalDate userModifyDate; //비밀번호 변경일 비밀번호 변경 시간 체크용

    private LocalDateTime userAccessTime; //마지막접속 휴면,해킹 체크용 마지막 접속일

    private GenderEnum userGender; //성별     남자 M 여자 F

    private BigInteger userMileage; //마일리지

    private String userRole = "USER"; //유저 권한
}
