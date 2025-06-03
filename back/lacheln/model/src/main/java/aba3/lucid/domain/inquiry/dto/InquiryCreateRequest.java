package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.enums.InquiryCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InquiryCreateRequest {

    @NotBlank(message = "문의 제목은 필수입니다.")
    private String inquiryTitle;

    @NotNull(message = "문의 카테고리는 필수입니다.")
    private InquiryCategory inquiryCategory;

    /**
     * HTML 형식의 텍스트만 입력받습니다. 이미지 삽입은 별도 API 없이 직접 URL 삽입으로 처리합니다.
     */
    @NotBlank(message = "문의 내용은 필수입니다.")
    private String inquiryContent;
}
