package aba3.lucid.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * 리뷰 작성 요청을 위한 DTO 클래스
 * - 결제 ID를 통해 구매자 여부를 확인
 * - 리뷰 내용과 평점은 필수 입력
 * - 이미지 URL 리스트는 선택 입력
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewCreateRequest {

    @NotNull(message = "결제 ID는 필수입니다.")
    private Long reviewId;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    private String rvContent;

    @NotNull(message = "리뷰 평점은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "리뷰 평점은 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", inclusive = true, message = "리뷰 평점은 5.0 이하여야 합니다.")
    private Double rvScore;

    private List<String> imageUrls; // 선택 항목
}

