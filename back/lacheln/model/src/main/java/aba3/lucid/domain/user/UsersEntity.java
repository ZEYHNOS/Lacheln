package aba3.lucid.domain.user;

import aba3.lucid.domain.user.enums.*;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private String userId; //소비자ID

    @ManyToOne(fetch = FetchType.LAZY)
    private CountryEntity country; //국가명코드 ISO 3166-1 alpha-2

    @Enumerated(EnumType.STRING)
    @Column(name = "user_language", columnDefinition = "CAHR(3)", nullable = false)
    private LanguageEnum userLanguage; //국가언어고유코드 ISO 639 alpha-3

    @Enumerated(EnumType.STRING)
    @Column(name = "user_currency", columnDefinition = "CAHR(3)", nullable = false)
    private CurrencyEnum userCurrency; //화폐단위 ISO 4217

    @Column(name = "user_email", length = 255, nullable = false)
    private String userEmail;

    @Column(name = "user_password", columnDefinition = "CHAR(255)")
    private String userPassword; //암호화 할거라 길이 255

    @Column(name = "user_name", length = 255, nullable = false)
    private String userName; //외국 이름 포함

    @Column(name = "user_nickname", columnDefinition = "CHAR(255)", nullable = false)
    private String userNickName; //닉네임

    @Column(name = "user_birthday", nullable = false)
    private LocalDate userBirthday; //생년월일

    @Enumerated(EnumType.STRING)
    @Column(name = "user_social", columnDefinition = "CHAR(1)", nullable = false)
    private SocialEnum userSocial; //로그인 수단 식별 로컬, 카카오, 구글, 애플(L, K, G, A)

    @Column(name = "user_phone", length = 20)
    private String userPhone; //전화번호

    @Column(name = "user_profile", columnDefinition = "CHAR(255)", nullable = false)
    private String userProfile; //프로필URL 디폴트 이미지 있음

    @Enumerated(EnumType.STRING)
    @Column(name = "user_tier", columnDefinition = "CHAR(30)", nullable = false)
    private TierEnum userTier; //챌린저,월드클래스,프로페셔널,세미프로,아마추어,admin

    @Enumerated(EnumType.STRING)
    @Column(name = "user_ads_notification", columnDefinition = "CHAR(1)", nullable = false)
    private NotificationEnum userAdsNotification; //알림 동의 여부 Y, N

    @Enumerated(EnumType.STRING)
    @Column(name = "user_account_status", columnDefinition = "CHAR(20)", nullable = false)
    private AccountStatusEnum userAccountStatus; //활성화, 비활성화, 휴면, 정지

    @CreationTimestamp
    @Column(name = "user_join_date", nullable = false)
    private LocalDate userJoinDate; //가입일

    // TODO 기본값 설정하기
    @Column(name = "user_modify_date", nullable = false)
    private LocalDate userModifyDate; //비밀번호 변경일 비밀번호 변경 시간 체크용

    @CreationTimestamp
    @Column(name = "user_access_time", nullable = false)
    private LocalDateTime userAccessTime; //마지막접속 휴면,해킹 체크용 마지막 접속일

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", columnDefinition = "CHAR(1)", nullable = false)
    private GenderEnum userGender; //성별     남자 M 여자 F

    @Column(name = "user_mileage", nullable = false, columnDefinition = "BIGINT")
    private BigInteger mileage; //마일리지

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
