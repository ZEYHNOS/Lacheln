package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.enums.InquiryCategory;
import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InquiryDetailResponse {

    private Long inquiryId;
    private String title;
    private String content;
    private String category;
    private String status;
    private String answer;
    private String createdAt;
    private String userEmail;  // 관리자용
}
