package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InquiryListResponse {

    private Long inquiryId;
    private String title;
    private InquiryStatus status;
    private LocalDateTime createdAt;

    // 관리자 전용: 일반 사용자에겐 null 처리 가능
    private String userEmail;
}