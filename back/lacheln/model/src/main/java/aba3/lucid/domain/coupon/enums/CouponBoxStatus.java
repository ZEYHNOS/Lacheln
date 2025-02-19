package aba3.lucid.domain.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponBoxStatus {
    UNUSED("미사용"),  // 쿠폰이 아직 사용되지 않음
    USED("사용"),      // 쿠폰이 사용됨
    EXPIRED("만료");   // 쿠폰이 만료됨

    private final String description;
}
