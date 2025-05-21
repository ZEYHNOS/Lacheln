package aba3.lucid.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 리뷰 답글 수정 요청 DTO
 * - 판매자 본인이 작성한 답글만 수정 가능
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewCommentUpdateRequest {

    @NotNull(message = "답글 ID는 필수입니다.")
    private Long rvCommentId; // 수정할 답글 ID

    @NotBlank(message = "수정할 내용은 필수입니다.")
    private String content; // 수정할 답글 본문
}