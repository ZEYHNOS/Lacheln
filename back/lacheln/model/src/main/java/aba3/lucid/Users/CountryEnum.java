package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryEnum {
    KOR("KR"),  // 한국
    USA("US"),  // 미국
    JPN("JP"),  // 일본
    CHN("CN");  // 중국

    private final String countryCode;
}
