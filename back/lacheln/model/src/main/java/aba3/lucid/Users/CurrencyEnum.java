package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {
    KRW("KOR"),  // 한국 원화
    USD("USA"),  // 미국 달러
    JPY("JPN"),  // 일본 엔화
    CNY("CHN");  // 중국 위안화

    private final String currencyCode;
}
