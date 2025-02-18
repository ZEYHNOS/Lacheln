package aba3.lucid.Users;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)") //UUID 36글자
    private String userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country_id", columnDefinition = "CHAR(2)", nullable = false)
    private CountryEnum countryId; //국가명코드 ISO 4217

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_language", columnDefinition = "CAHR(3)", nullable = false)
    private LanguageEnum userLanguage; //국가언어고유코드 ISO 639 alpha-3

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_currency", columnDefinition = "CAHR(3)", nullable = false)
    private CurrencyEnum userCurrency; //화폐단위 ISO 4217

    @NotNull
    @Column(name = "user_email", length = 255, nullable = false)
    private String userEmail;

    @Column(name = "user_password", columnDefinition = "CHAR(255)")
    private String userPassword; //암호화 할거라 길이 255

    @NotNull
    @Column(name = "user_name", length = 255, nullable = false)
    private String userName; //외국 이름 포함

    @NotNull
    @Column(name = "user_nickname", columnDefinition = "CHAR(30)", nullable = false)
    private String userNickName;

    @NotNull
    @Column(name = "user_birthday", nullable = false)
    private LocalDate userBirthday;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_social", columnDefinition = "CHAR(1)", nullable = false)
    private SocialEnum userSocial; //로그인 수단 식별 로컬, 카카오, 구글, 애플(L, K, G, A)

    @Column(name = "user_phone", length = 20)
    private String userPhone; //국제전화번호 생각

    @NotNull
    @Column(name = "user_profile", columnDefinition = "CHAR(255)", nullable = false)
    private String userProfile; //기본 이미지 넣을 예정

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_tier", columnDefinition = "CHAR(30)", nullable = false)
    private TierEnum userTier; //챌린저,월드클래스,프로페셔널,세미프로,아마추어,admin

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_ads_notification", columnDefinition = "CHAR(1)", nullable = false)
    private NotificationEnum userAdsNotification; //알림 동의 여부 Y, N

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_account_status", columnDefinition = "CHAR(20)", nullable = false)
    private AccountStatusEnum userAccountStatus; //활성화, 비활성화, 휴면, 정지

    @NotNull
    @Column(name = "user_join_date", nullable = false)
    private LocalDate userJoinDate; //가입일

    @NotNull
    @Column(name = "user_modify_date", nullable = false)
    private LocalDate userModifyDate; //비밀번호 변경 시간 체크용 비번 수정일

    @NotNull
    @Column(name = "user_access_time", nullable = false)
    private LocalDateTime userAccessTime; //휴면,해킹 체크용 마지막 접속일

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", columnDefinition = "CHAR(1)", nullable = false)
    private GenderEnum userGender; //남자 M  여자 F

    @NotNull
    @Column(name = "user_mileage", nullable = false)
    private BigInteger mileage;

    /*
     * 시간 기반 UUIDv1을 생성하여 userId로 설정한 새로운 UsersEntity 객체를 반환한다.
     *
     * 이 메서드는 `com.fasterxml.uuid.Generators.timeBasedGenerator()`를 사용하여
     * 현재 시간 정보를 기반으로 고유한 UUID를 생성한다. UUIDv1은 시간 순서대로 생성되므로
     * 각 `userId`가 고유하고 시간이 순차적으로 정렬되는 특징을 가진다.
    */
    public static UsersEntity createWithUUIDv1() {
        return UsersEntity.builder()
                .userId(Generators.timeBasedGenerator().generate().toString())
                .build();
    }
}
