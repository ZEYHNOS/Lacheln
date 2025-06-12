package aba3.lucid.coupon.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.coupon.business.CouponBoxBusiness;
import aba3.lucid.coupon.business.CouponBusiness;
import aba3.lucid.coupon.service.CouponService;
import aba3.lucid.domain.coupon.dto.CouponBoxResponse;
import aba3.lucid.domain.coupon.dto.CouponResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/couponbox")
public class CouponBoxController {

    private final CouponBoxBusiness couponBoxBusiness;

    @GetMapping
    public API<List<CouponBoxResponse>> getBox(
            @AuthenticationPrincipal CustomUserDetails user
    )   {
        return couponBoxBusiness.getBoxByUser(user);
    }
}
