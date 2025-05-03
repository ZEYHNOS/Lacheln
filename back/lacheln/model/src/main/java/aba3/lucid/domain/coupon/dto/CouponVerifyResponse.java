package aba3.lucid.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponVerifyResponse {

    Map<Long,Integer> keyCompanyIdValueDiscountRate;

}
