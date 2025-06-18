package aba3.lucid.domain.user.entity;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.UserCode;
import aba3.lucid.domain.user.dto.UserUpdateRequest;
import aba3.lucid.domain.user.enums.*;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_country", columnDefinition = "CHAR(2)", nullable = false)
    private CountryEnum userCountry; //국가명코드 ISO 3166-1 alpha-2

    @Enumerated(EnumType.STRING)
    @Column(name = "user_language", columnDefinition = "CHAR(3)", nullable = false)
    private LanguageEnum userLanguage; //국가언어고유코드 ISO 639 alpha-3

    @Enumerated(EnumType.STRING)
    @Column(name = "user_currency", columnDefinition = "CHAR(3)", nullable = false)
    private CurrencyEnum userCurrency; //화폐단위 ISO 4217

    @Column(name = "user_email", length = 255, nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", columnDefinition = "CHAR(255)")
    private String userPassword; //암호화 할거라 길이 255

    @Column(name = "user_name", length = 255, nullable = false)
    private String userName; //외국 이름 포함

    @Column(name = "user_nickname", columnDefinition = "CHAR(255)", nullable = false, unique = true)
    private String userNickName; //닉네임

    @Column(name = "user_birthday")
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
    private BigInteger userMileage; //마일리지

    @Column(name = "user_role", nullable = false, columnDefinition = "CHAR(5)")
    private String userRole; //유저 권한

    // 비밀번호 변경
    public void updatePassword(String newPassword) {
        if(newPassword == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "비밀번호 기입이 안되었습니다.");
        }
        this.userPassword = newPassword;
    }

    // 닉네임 변경
    public void updateNickName(String newNickName) {
        if(newNickName == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "닉네임을 입력해주세요.");
        }
        this.userNickName = newNickName;
    }

    // 연락처 변경
    public void updatePhone(String newPhone)    {
        this.userPhone = newPhone;
    }

    // 언어 변경
    public void updateLanguage(LanguageEnum newLanguage) {
        if(newLanguage == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "언어 설정을 해주세요.");
        }
        this.userLanguage = newLanguage;
    }

    // 화폐 단위 변경
    public void updateCurrency(CurrencyEnum newCurrency) {
        if(newCurrency == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "화폐단위 설정을 해주세요.");
        }
        this.userCurrency = newCurrency;
    }

    // 푸쉬 알람 여부 변경
    public void updateAdsNotification(NotificationEnum newAdsNotification) {

        if(newAdsNotification == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "푸쉬 알람 여부가 null 입니다.");
        }
        this.userAdsNotification = newAdsNotification;
    }

    public void updateProfileImage(String newProfileImage) {
        if(newProfileImage == null) {
            this.userProfile = "/image/default.jpg";
            return;
        }
        this.userProfile = newProfileImage;
    }

    public void updateUserName(String newUserName) {
        if(newUserName == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "이름은 필수 입력 사항입니다.");
        }
        this.userName = newUserName;
    }

    public void updateGender(GenderEnum newGender) {
        if(newGender == null) {
            throw new ApiException(UserCode.CANNOT_FIND_DATA, "성별을 필수 입력사항 입니다.");
        }
        this.userGender = newGender;
    }

    // 유저정보 업데이트
    public void updateUser(UserUpdateRequest userUpdateRequest, BCryptPasswordEncoder bCryptPasswordEncoder)    {
        log.info("UserUpdateRequest: {}", userUpdateRequest.getGender());
        if(!userUpdateRequest.getPassword().equals("NULL")) {
            updatePassword(bCryptPasswordEncoder.encode(userUpdateRequest.getPassword()));
        }
        updateUserName(userUpdateRequest.getName());
        updateNickName(userUpdateRequest.getNickname());
        updatePhone(userUpdateRequest.getPhone());
        updateLanguage(userUpdateRequest.getLanguage());
        updateCurrency(userUpdateRequest.getCurrency());
        updateAdsNotification(userUpdateRequest.getNotification());
        updateProfileImage(userUpdateRequest.getImage());
        updateGender(userUpdateRequest.getGender());
    }

    /*
     * 시간 기반 UUIDv1을 생성하여 userId로 설정한 새로운 UsersEntity 객체를 반환한다.
     *
     * 이 메서드는 `com.fasterxml.uuid.Generators.timeBasedGenerator()`를 사용하여
     * 현재 시간 정보를 기반으로 고유한 UUID를 생성한다. UUIDv1은 시간 순서대로 생성되므로
     * 각 `userId`가 고유하고 시간이 순차적으로 정렬되는 특징을 가진다.
    */
    public static String createWithUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public void setMileage(BigInteger mileage) {
        if (mileage == null) {
            return;
        }

        this.userMileage = mileage;
    }

    public void addMileage(BigInteger payTotalPrice) {
        BigDecimal priceDecimal = new BigDecimal(payTotalPrice);
        BigDecimal percent = new BigDecimal("0.015"); // 1.5%
        BigDecimal mileageDecimal = priceDecimal.multiply(percent);

        BigInteger mileage = mileageDecimal.setScale(0, BigDecimal.ROUND_DOWN).toBigInteger();

        this.userMileage = this.userMileage.add(mileage);
    }

    public boolean upgradeTierAfterPayment(TierEnum tierEnum) {
        if (this.userTier.getRank() >= tierEnum.getRank()) {
            return false;
        }

        this.userTier = tierEnum;
        return true;
    }
}
