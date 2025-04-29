package aba3.lucid.domain.coupon.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.user.entity.UsersEntity;

@Converter
public class CouponBoxConverter {

    public CouponBoxEntity toEntity(CouponEntity coupon, String userId) {
        return CouponBoxEntity.builder()
                .coupon(coupon)
                .userId(userId)
                .couponStatus(CouponBoxStatus.UNUSED)
                .build()
                ;
    }

}
