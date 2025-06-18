package aba3.lucid.domain.payment.dto;

import aba3.lucid.common.annotation.ValidList;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentRequest {

    // merchantId
    @NotBlank
    private String payId;

    // 결제 방식
    @NotBlank
    private String payTool;

    // 총 금액(할인된 금액으로)
    @NotNull
    private BigInteger payTotalPrice;

    // 상태
    @NotNull
    private PaymentStatus payStatus;

    // 마일리지
    @NotNull
    private BigInteger payMileage;

    // 업체에서 제공하는 Uid
    @NotBlank
    private String payImpUid;

    // 결제 일시
    @NotNull
    private LocalDateTime paidAt;

    // 장바구니 ID 리스트
    @ValidList
    private List<Long> cartIdList;

}
