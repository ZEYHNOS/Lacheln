package aba3.lucid.domain.review.dto;

import aba3.lucid.domain.product.enums.ReviewStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자에게 보여줄 리뷰 응답 DTO
 * - 리뷰 ID는 노출하지 않음
 * - 작성자 닉네임, 리뷰 내용, 평점, 이미지, 작성일시만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewResponse {

    private Long reviewId;

    private String nickname;                // 작성자 닉네임

    private Long productId;

    private String productName;

    private Long companyId;

    private ReviewStatus status;

    private String content;                 // 리뷰 내용

    private Double score;                   // 리뷰 평점

    private List<String> imageUrlList;         // 첨부 이미지 URL 리스트

    private LocalDateTime createdAt;        // 리뷰 작성일시

}
