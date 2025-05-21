package aba3.lucid.domain.review.convertor;


import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class ReviewCommentConvertor {

    public ReviewCommentEntity toEntity(CompanyEntity company, ReviewEntity reviewId, ReviewCommentRequest request){

        ReviewCommentEntity comment = ReviewCommentEntity.builder()
                .rvcContent(request.getContent())
                .rvcStatus(request.getStatus())
                .rvcCreate(LocalDate.now())
                .company(company)
                .review(reviewId)
                .build();

        return comment;

    }

    public ReviewCommentResponse toResponse(ReviewCommentEntity entity) {
        return ReviewCommentResponse.builder()
                .commentId(entity.getRvCommentId())
                .content(entity.getRvcContent())
                .status(entity.getRvcStatus())
                .cpId(entity.getCompany().getCpId())
                .reviewId(entity.getReview().getReviewId())
                .createdAt(entity.getRvcCreate())
                .build();
    }



}
