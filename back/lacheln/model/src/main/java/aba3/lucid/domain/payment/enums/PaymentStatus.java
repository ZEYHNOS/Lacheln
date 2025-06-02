package aba3.lucid.domain.payment.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("납부됐었습니다"),
    CANCEL("취소됐습니다"),
    REFUND("")
    ;
    private final String description;


}
