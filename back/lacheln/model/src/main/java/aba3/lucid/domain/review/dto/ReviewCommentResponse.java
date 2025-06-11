package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.product.enums.ReviewStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class ReviewCommentResponse {
    private Long commentId;
    private Long cpId;
    private Long reviewId;
    private String content;
    private LocalDateTime createdAt;
    private ReviewStatus status;
}
