package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.product.enums.ReviewCommentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
public class ReviewCommentResponse {
    private long commentId;
    private long cpId;
    private long reviewId;
    private String content;
    private LocalDate createdAt;
    private ReviewCommentStatus status;
}
