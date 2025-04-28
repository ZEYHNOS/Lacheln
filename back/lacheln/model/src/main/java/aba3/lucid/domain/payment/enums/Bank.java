package aba3.lucid.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bank {

    KBS_BANK("국민은행"),
    SHINHAN_BANK("신한은행"),
    KEB_HANA_BANK("KEB 하나은행"),
    WOORI_BANK("우리은행"),
    NH_BANK("농협은행"),
    BUSAN_BANK("부산은행"),
    DGB_BANK("DGB 대구은행"),
    IBK_BANK("산업은행"),
    KAKAO_BANK("카카오뱅크"),
    KTB_BANK("KTB은행"),
    SUHYUP_BANK("수협은행"),
    WOOJIN_BANK("우진은행");

    private final String bankName;
}