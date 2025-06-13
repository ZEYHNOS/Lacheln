package aba3.lucid.coupon.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.coupon.service.CouponBoxService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.coupon.convertor.CouponBoxConverter;
import aba3.lucid.domain.coupon.dto.CouponBoxResponse;
import aba3.lucid.domain.coupon.entity.CouponBoxEntity;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Business
@RequiredArgsConstructor
public class CouponBoxBusiness {

    private final CouponBoxService couponBoxService;
    private final CompanyService companyService;
    private final CouponBoxConverter couponBoxConverter;

    public API<List<CouponBoxResponse>> getBoxByUser(CustomUserDetails user)  {
        if(user.getRole().equals("USER")) {
            String userId = user.getUserId();
            List<CouponBoxEntity> coupons = couponBoxService.getAllCouponBox(userId);
            List<CouponBoxResponse> responses = new ArrayList<>();

            for (CouponBoxEntity couponBox : coupons) {
                CompanyEntity company = companyService.findByIdWithThrow(couponBox.getCoupon().getCompany().getCpId());
                CouponBoxResponse box = couponBoxConverter.toDto(couponBox, company);
                responses.add(box);
            }

            return API.OK(responses);
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "사용자만 couponBox를 조회할 수 있습니다.");
        }
    }
}
