package aba3.lucid.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponVerifyRequest {

    private String userId;

    private List<Long> couponBoxIdList;

    private List<Long> productIdList;

    private Map<Long, BigInteger> amount;

}
