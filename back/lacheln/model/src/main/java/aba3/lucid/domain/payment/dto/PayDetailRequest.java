package aba3.lucid.domain.payment.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayDetailRequest {

    // 사용자의 쿠폰 Id
    private Long couponBoxId;

    // 해당 상품의 업체 id
    private Long cpId;

    // 상품 이름
    private String productName;

    // 상품 가격
    private BigInteger payCost;

    // 상품 할인 가격
    private BigInteger payDcPrice;

    // 일정 날짜
    private LocalDateTime scheduleDate;

    // 해당 상품 옵션 리스트
    private List<PayDetailOptionRequest> payDetailOptionEntityList;

}
