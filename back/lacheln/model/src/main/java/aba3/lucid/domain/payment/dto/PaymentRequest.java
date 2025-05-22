package aba3.lucid.domain.payment.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequest {

    // merchantId
    private String payId;

    // 결제 방식
    private String payTool;

    // 총 금액(할인된 금액으로)
    private BigInteger payTotalPrice;

    // 할인하는 금액
    private BigDecimal payDcPrice;

    // 상태
    private String payStatus;

    // 마일리지
    private BigInteger payMileage;

    // 업체에서 제공하는 Uid
    private String payImpUid;

    // 결제 일시
    private LocalDateTime paidAt;

    // 결제하는 상품 리스트
    private List<PayDetailRequest> payDetailRequestList;

    // 장바구니 ID 리스트
    private List<Long> cartIdList;

}
