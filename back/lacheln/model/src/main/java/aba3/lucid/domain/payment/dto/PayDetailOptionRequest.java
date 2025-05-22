package aba3.lucid.domain.payment.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayDetailOptionRequest {

    // 상품 옵션 이름
    private String pdOpName;

    // 상품 옵션 상세 이름
    private String pdOpDtName;

    // 수량
    private int quantity;

    // 옵션 추가 작업 시간
    private LocalTime taskTime;

    // 가격(1개)
    private BigInteger payOpPlusCost;

}
