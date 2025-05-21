package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.enums.InquiryStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 내 문의 내역 응답 DTO
 * - 작성한 문의들의 목록 조회에 사용
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InquiryResponse {

    private Long inquiryId;
    private String title;
    private String category;
    private String content;
    private InquiryStatus status;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}