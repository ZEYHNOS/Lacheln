package aba3.lucid.payment.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.calendar.dto.CalendarDto;
import aba3.lucid.domain.payment.dto.*;
import aba3.lucid.payment.business.PaymentBusiness;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
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
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        PayManagementResponse response = paymentBusiness.save(user.getUserId(), request);
        return API.OK(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "결제 전 검증 및 결제해야 하는 금액 반환", description = "결제 전 쿠폰, 마일리지, 상품 스냅샷 유효성 검사 및 총 결제하는 금액 반환")
    public API<BigInteger> paymentVerification(
            @RequestBody PaymentVerifyRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        BigInteger totalAmount = paymentBusiness.verificationAndGetTotalAmount(request, user.getUserId());
        return API.OK(totalAmount);
    }

    @GetMapping("/user/list")
    @Operation(summary = "사용자의 결제 내역")
    public API<List<PayDetailResponse>> getUserPaymentList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<PayDetailResponse> payManagementResponseList = paymentBusiness.getPaymentList(user.getUserId());

        return API.OK(payManagementResponseList);
    }

    @GetMapping("/company/list")
    @Operation(summary = "업체에 결제한 내역")
    public API<List<PayDetailResponse>> getCompanyPaymentList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<PayDetailResponse> payManagementResponseList = paymentBusiness.getPaymentList(user.getCompanyId());
        return API.OK(payManagementResponseList);
    }

    @GetMapping("/block")
    @Operation(summary = "상품ID, 날짜로 조회한 결제 내역 리스트")
    public API<List<PayDetailBlockResponse>> getBlockDateTimeList(
            @RequestParam Long pdId,
            @RequestParam LocalDate date
    )   {
        List<PayDetailBlockResponse> payManagementResponseList = paymentBusiness.getPaymentPdIdAndDate(pdId, date);
        return API.OK(payManagementResponseList);
    }
}
