package aba3.lucid.coupon.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.coupon.service.CouponService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.convertor.CouponConvertor;
import aba3.lucid.domain.coupon.dto.CouponRequest;
import aba3.lucid.domain.coupon.dto.CouponResponse;
import aba3.lucid.domain.coupon.entity.CouponEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@RequiredArgsConstructor
public class CouponBusiness {

    private final CouponService couponService;
    private final CompanyService companyService;
    private final ProductService productService;

    private final CouponConvertor couponConvertor;

    // 쿠폰 등록


    // 유저가 쿠폰 등록하기


    // 유저가 쿠폰 사용


    // 쿠폰 삭제


    // 쿠폰 수정
    public CouponResponse updateCoupon(CouponRequest request, Long companyId, Long productId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(companyId, productId);

        // 업체, 상품 Entity 가지고 오기 (Product는 Null 허용)
        CompanyEntity company = companyService.findByIdWithThrow(companyId);
        ProductEntity product = null;
        if (productId != null) {
            product = productService.findByIdWithThrow(productId);
        }

        // DTO -> Entity
        CouponEntity coupon = couponConvertor.toEntity(request, company, product);
        couponService.validateCompanyCouponOwnership(company, coupon);

        // Entity -> DTO
        CouponEntity newCouponEntity = couponService.updateCoupon(coupon);
        return couponConvertor.toResponse(newCouponEntity);
    }

}
