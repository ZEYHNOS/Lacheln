package aba3.lucid.domain.review.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCommentEventDto {
    private Long reviewId;
    private Long cpId;
    private String userId;

    public static ReviewCommentEventDto deleteRequest(Long reviewId) {
        return ReviewCommentEventDto.builder()
                .reviewId(reviewId)
                .build()
                ;
    }

    public static ReviewCommentEventDto createBaseReviewCommentRequest(Long cpId, Long reviewId, String userId) {
        return ReviewCommentEventDto.builder()
                .cpId(cpId)
                .reviewId(reviewId)
                .userId(userId)
                .build()
                ;
    }

}
