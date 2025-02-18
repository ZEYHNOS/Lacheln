package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryEnum {
    KR("KOR"),  // 한국
    US("USA"),  // 미국
    JP("JPN"),  // 일본
    CN("CHN");  // 중국

    private final String countryCode;
}
