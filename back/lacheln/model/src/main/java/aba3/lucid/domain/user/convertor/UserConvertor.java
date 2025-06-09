package aba3.lucid.domain.user.convertor;

import aba3.lucid.domain.user.dto.UserCheckResponse;
import aba3.lucid.domain.user.dto.UserDto;
import aba3.lucid.domain.user.dto.UserSignupRequest;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserConvertor {

    private final BCryptPasswordEncoder passwordEncoder;

    public UsersEntity convertUserSignupToUserEntity(UserSignupRequest user) {
        return UsersEntity.builder()
                .userId(UsersEntity.createWithUUIDv1())
                .userCountry(CountryEnum.KR)
                .userLanguage(LanguageEnum.KOR)
                .userCurrency(CurrencyEnum.KRW)
                .userEmail(user.getEmail())
                .userPassword(passwordEncoder.encode(user.getPassword()))
                .userName(user.getName())
                .userNickName(user.getNickName())
                .userSocial(SocialEnum.L)
                .userPhone(user.getPhone())
                .userProfile("default.jpg")
                .userTier(TierEnum.SEMI_PRO)
                .userAdsNotification(user.getAdsNotification())
                .userAccountStatus(AccountStatusEnum.ACTIVE)
                .userJoinDate(LocalDate.now())
                .userModifyDate(LocalDate.now())
                .userAccessTime(LocalDateTime.now())
                .userBirthday(user.getBirthDate())
                .userGender(user.getGender())
                .userMileage(new BigInteger("0"))
                .userRole("USER")
                .build();
    }

    public UserDto convertEntityToDto(UsersEntity user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .country(user.getUserCountry())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .phone(user.getUserPhone())
                .tier(user.getUserTier())
                .nickname(user.getUserNickName())
                .notification(user.getUserAdsNotification())
                .gender(user.getUserGender())
                .currency(user.getUserCurrency())
                .language(user.getUserLanguage())
                .build();
    }

    public UserCheckResponse entityToCheckResponse(UsersEntity user) {
        return UserCheckResponse.builder()
                .userId(user.getUserId())
                .email(user.getUserEmail())
                .name(user.getUserName())
                .phone(user.getUserPhone())
                .gender(user.getUserGender())
                .tier(user.getUserTier())
                .currency(user.getUserCurrency())
                .mileage(user.getUserMileage())
                .nickname(user.getUserNickName())
                .notification(user.getUserAdsNotification())
                .language(user.getUserLanguage())
                .profileImageUrl(user.getUserProfile())
                .build();
    }
}
