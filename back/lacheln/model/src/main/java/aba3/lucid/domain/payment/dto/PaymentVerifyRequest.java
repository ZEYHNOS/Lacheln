package aba3.lucid.domain.payment.dto;

import aba3.lucid.common.annotation.ValidList;
import aba3.lucid.domain.cart.dto.CartPaymentRequest;
import aba3.lucid.domain.cart.dto.CartRequest;
import aba3.lucid.domain.product.dto.ProductCouponVerifyDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentVerifyRequest {
    // 쿠폰함 ID
    @ValidList
    private List<Long> couponBoxIdList;

    // 사용할려고 하는 마일리지 금액
    @NotNull
    private BigInteger mileage;

    // 장바구니 리스트
    @ValidList
    private List<Long> cartIdList;
}
