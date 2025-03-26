package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryEnum {
    KR("대한민국", "kor.jpg"),  // 한국
    US("UnitedState America", "usa.jpg"),  // 미국
    JP("日本", "japan.jpg"),  // 일본
    CN("中国", "china.jpg");  // 중국

    private final String countryName;
    private final String countryUrl;
}
