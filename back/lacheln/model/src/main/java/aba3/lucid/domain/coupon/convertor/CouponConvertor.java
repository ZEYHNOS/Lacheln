package aba3.lucid.domain.coupon.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.dto.CouponRequest;
import aba3.lucid.domain.coupon.dto.CouponResponse;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.product.entity.ProductEntity;

@Converter
public class CouponConvertor {

    public CouponEntity toEntity(CouponRequest request, CompanyEntity company) {
        return CouponEntity.builder()
                .couponName(request.getName())
                .couponContent(request.getContent())
                .couponDiscountRate(request.getDiscountRate())
                .couponMinimumCost(request.getMinimumCost())
                .couponMaximumCost(request.getMaximumCost())
                .couponCreateDate(request.getCreateDate())
                .couponExpirationDate(request.getExpirationDate())
                .company(company)
                .build()
                ;
    }

    public CouponResponse toResponse(CouponEntity entity) {
        return CouponResponse.builder()
                .couponId(entity.getCouponId())
                .couponName(entity.getCouponName())
                .content(entity.getCouponContent())
                .discountRate(entity.getCouponDiscountRate())
                .minimumCost(entity.getCouponMinimumCost())
                .maximumCost(entity.getCouponMaximumCost())
                .createDate(entity.getCouponCreateDate())
                .expirationDate(entity.getCouponExpirationDate())
                .companyName(entity.getCompany().getCpName())
                .build()
                ;
    }

}
