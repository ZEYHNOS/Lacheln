package aba3.lucid.domain.coupon.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Builder
public class CouponBoxResponse {
    private Long boxId;
    private String couponId;
    private Long companyId;
    private String companyName;
    private String couponName;
    private String couponContent;
    private Integer couponDiscountRate;
    private BigInteger couponMinimumCost;
    private BigInteger couponMaximumCost;
    private LocalDateTime couponCreateDate;
    private LocalDateTime couponExpirationDate;
}
