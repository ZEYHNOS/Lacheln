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
}
