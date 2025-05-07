package aba3.lucid.payment.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.domain.payment.dto.PayDetailResponse;
import aba3.lucid.domain.payment.dto.PayManagementResponse;
import aba3.lucid.domain.payment.dto.PaymentRequest;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.payment.business.PaymentBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@RequestMapping("/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentBusiness paymentBusiness;

    @PostMapping("/merchant")
    @Operation(summary = "MerchantId 발급")
    public API<String> getMerchantUid() {
        return API.OK(paymentBusiness.getMerchantUid());
    }

    @PostMapping("/save")
    public API<PayManagementResponse> savedPayment(
            @RequestBody PaymentRequest request
    ) {
        PayManagementResponse response = paymentBusiness.save(AuthUtil.getUserId(), request);

        return API.OK(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "결제 전 검증 및 총액", description = "결제 전 쿠폰, 마일리지, 상품 스냅샷 유효성 검사 및 총 결제하는 금액 반환")
    public API<BigInteger> paymentVerification(
            @RequestBody PaymentVerifyRequest request
    ) {
        BigInteger totalAmount = paymentBusiness.verificationAndGetTotalAmount(request, AuthUtil.getUserId());

        return API.OK(totalAmount);
    }

    @GetMapping("/user/list")
    @Operation(summary = "사용자의 결제 내역")
    public API<List<PayManagementResponse>> getUserPaymentList(
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        List<PayManagementResponse> payManagementResponseList = paymentBusiness.getPaymentList(customAuthenticationToken.getUserId());
        List<PayManagementResponse> payManagementResponseList = paymentBusiness.getPaymentList("");

        return API.OK(payManagementResponseList);
    }

    @GetMapping("/company/list")
    @Operation(summary = "업체에 결제한 내역")
    public API<List<PayDetailResponse>> getCompanyPaymentList(
            @AuthenticationPrincipal CustomAuthenticationToken customAuthenticationToken
    ) {
//        List<PayManagementResponse> payManagementResponseList = paymentBusiness.getPaymentList(customAuthenticationToken.getCompanyId());
        List<PayDetailResponse> payManagementResponseList = paymentBusiness.getPaymentList(1L);

        return API.OK(payManagementResponseList);
    }

}
