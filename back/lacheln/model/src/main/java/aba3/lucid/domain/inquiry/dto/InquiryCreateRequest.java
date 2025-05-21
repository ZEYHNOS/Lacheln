package aba3.lucid.domain.inquiry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * 문의 작성 요청 DTO
 * - 제목, 카테고리, 내용, 첨부 이미지 URL 리스트를 포함
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InquiryCreateRequest {

    @NotBlank(message = "문의 제목은 필수입니다.")
    private String inquiryTitle;

    @NotBlank(message = "문의 카테고리는 필수입니다.")
    private String inquiryCategory;

    @NotBlank(message = "문의 내용은 필수입니다.")
    private String inquiryContent;

    private List<String> imageUrls; // 이미지 URL 리스트 (선택)
}