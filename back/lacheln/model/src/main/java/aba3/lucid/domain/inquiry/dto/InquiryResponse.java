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
public class InquiryResponse {

    private Long inquiryId;
    private String title;
    private InquiryCategory category;
    private String content;
    private InquiryStatus status;
    private LocalDateTime createdAt;
}
