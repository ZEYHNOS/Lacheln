package aba3.lucid.Payment;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStatus {
    Paid ("납부됐었습니다"),
    Cancel ("취소됐습니다"),
    ;
    private final String description;


}
