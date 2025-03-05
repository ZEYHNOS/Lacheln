package aba3.lucid.domain.inquiry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryStatus {
    RECEIVED("접수"),     // 접수
    IN_PROGRESS("진행중"), // 진행중
    COMPLETED("완료");    // 완료

    private final String inquiryStatus;
}
