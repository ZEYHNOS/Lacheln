package aba3.lucid.payment.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.payment.dto.PayManagementResponse;
import aba3.lucid.domain.payment.dto.PaymentRequest;
import aba3.lucid.domain.payment.dto.PaymentVerifyRequest;
import aba3.lucid.payment.business.PaymentBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

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

}
