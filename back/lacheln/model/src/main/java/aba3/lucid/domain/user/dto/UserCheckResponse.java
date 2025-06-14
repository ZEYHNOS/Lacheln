package aba3.lucid.domain.user.dto;

import aba3.lucid.domain.user.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@Builder
public class UserCheckResponse {
    private String userId;
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private TierEnum tier;
    private SocialEnum social;
    private NotificationEnum notification;
    private GenderEnum gender;
    private BigInteger mileage;
    private LanguageEnum language;
    private CurrencyEnum currency;
    private String image;
}
