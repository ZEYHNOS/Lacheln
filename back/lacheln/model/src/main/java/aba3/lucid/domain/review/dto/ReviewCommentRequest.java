package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.product.enums.CommentStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentRequest {

    private Long reviewId;

    private Long cpId;

    private String content;

    private LocalDate createdAt;


    @Builder.Default
    private CommentStatus status = CommentStatus.VISIBLE ;


}
