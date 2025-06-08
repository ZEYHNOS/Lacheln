package aba3.lucid.domain.inquiry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 문의 카테고리 종류
 * - 프론트에서 선택할 수 있는 항목들
 */
@Getter
@AllArgsConstructor
public enum InquiryCategory {

    ACCOUNT("계정"),   // 계정 관련 문의
    PAYMENT("결제"),   // 결제 관련 문의
    ADVERTISEMENT("광고"), // 광고 관련 문의
    EVENT("이벤트"); // 이벤트 관련 문의

    private final String label;
}