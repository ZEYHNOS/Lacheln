package aba3.lucid.domain.payment.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailResponse {

    // 결제 상세 ID
    private Long payDetailId;

    // 업체 ID
    private Long cpId;

    // 유저 ID
    private String userId;

    // 유저 이름
    private String userName;

    // 쿠폰 이름
    private String couponName;

    // 상품 ID
    private Long pdId;

    // 상품 이름
    private String pdName;

    // 가격
    private BigInteger payCost;

    // 결제 일시
    private LocalDateTime paidAt;

    // 일정 일시
    private LocalDateTime scheduleAt;

    // 상태
    private PaymentStatus status;

    // 환불 금액
    private BigInteger refundPrice;

    // 카테고리
    private CompanyCategory category;

    // 옵션
    private List<PayDetailOptionResponse> options;

}