package aba3.lucid.user.model;

import aba3.lucid.enums.Currency;
import aba3.lucid.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String userId;

    // TODO Entity 만들기
    private String countryId;

    // TODO 국가 더 추가하기
    @Enumerated(EnumType.STRING)
    private Language language;

    // TODO ENUM 제대로 만들기
    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String email;

    private String password;

    private String name;

    private String nickName;

    private LocalDate birthday;

    // TODO ENUM
    @Enumerated(EnumType.STRING)
    private String social;

    private String phone;

    private String profile;

    private boolean auth;

    // TODO enum
    @Enumerated(EnumType.STRING)
    private String role;

    // TODO ENUM or int(String)
    @Enumerated(EnumType.STRING)
    private String tier;

    private String adsNotification;

    // TODO enum
    @Enumerated(EnumType.STRING)
    private String accountStatus;

    private LocalDateTime joinDate;

    private LocalDateTime modifyDate;

    private LocalDateTime accessTime;

    private String gender;

    // TODO 1조 털어보자
    private String mileage;

    // TODO ENUM or int(String)
    @Enumerated(EnumType.STRING)
    private String purpose;

}