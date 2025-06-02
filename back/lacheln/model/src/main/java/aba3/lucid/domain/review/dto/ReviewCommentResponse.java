package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.product.enums.CommentStatus;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate createdAt;
    private CommentStatus status;
}
