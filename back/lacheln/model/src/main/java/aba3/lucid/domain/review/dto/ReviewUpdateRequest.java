package aba3.lucid.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * 리뷰 수정 요청을 위한 DTO
 * - 리뷰 내용, 평점, 이미지 리스트를 수정할 수 있음
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewUpdateRequest {

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String rvContent; // 수정할 리뷰 본문

    @NotNull(message = "리뷰 평점은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "평점은 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", inclusive = true, message = "평점은 5.0 이하여야 합니다.")
    private Double rvScore; // 수정할 평점

    private List<String> imageUrls; // 수정할 이미지 URL 리스트 (선택)
}
