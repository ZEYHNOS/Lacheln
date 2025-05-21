package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.enums.ReviewCommentStatus;
import aba3.lucid.domain.review.entity.ReviewEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentRequest {

    private ReviewEntity reviewId;

    private CompanyEntity cpId;

    private String content;

    private LocalDate createdAt;


    @Builder.Default
    private ReviewCommentStatus status = ReviewCommentStatus.VISIBLE ;


}
