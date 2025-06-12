package aba3.lucid.domain.coupon.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.dto.CouponBoxResponse;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.List;

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

    public CouponBoxResponse toDto(CouponBoxEntity couponBox, CompanyEntity company)   {
        CouponEntity coupon = couponBox.getCoupon();
        return CouponBoxResponse.builder()
                .boxId(couponBox.getCouponBoxId())
                .couponContent(coupon.getCouponContent())
                .couponCreateDate(coupon.getCouponCreateDate())
                .couponId(coupon.getCouponId())
                .couponMaximumCost(coupon.getCouponMaximumCost())
                .couponMinimumCost(coupon.getCouponMinimumCost())
                .couponName(coupon.getCouponName())
                .couponExpirationDate(coupon.getCouponExpirationDate())
                .couponDiscountRate(coupon.getCouponDiscountRate())
                .companyName(company.getCpName())
                .companyId(company.getCpId())
                .build();
    }
}
