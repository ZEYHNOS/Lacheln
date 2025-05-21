package aba3.lucid.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 리뷰 답글 작성 요청 DTO
 * - 해당 상품의 판매자만 작성 가능
 * - 하나의 리뷰에 대해 한 업체당 하나의 답글만 작성 가능
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewCommentCreateRequest {

    @NotNull(message = "리뷰 ID는 필수입니다.")
    private Long reviewId; // 답글을 달 리뷰 ID

    @NotBlank(message = "답글 내용은 필수입니다.")
    private String content; // 답글 내용
}