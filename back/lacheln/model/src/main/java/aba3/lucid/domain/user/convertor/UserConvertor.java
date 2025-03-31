package aba3.lucid.domain.user.convertor;

import aba3.lucid.common.password.CustomPasswordEncoder;
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
}
