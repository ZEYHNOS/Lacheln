package aba3.lucid.domain.product.dto;

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
public class ProductCouponVerifyDto {

    private Long productId;

    private String productName;

    private BigInteger amount;

    private List<Long> productDetailIdList;
}
