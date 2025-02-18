package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {
    KOR("KRW"),  // 한국 원화
    USA("USD"),  // 미국 달러
    JPN("JPY"),  // 일본 엔화
    CHN("CNY");  // 중국 위안화

    private final String currencyCode;
}
