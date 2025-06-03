package aba3.lucid.domain.review.converter;


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
public class ReviewCommentConverter {

    public ReviewCommentEntity toEntity(CompanyEntity company, ReviewEntity reviewId, ReviewCommentRequest request){

        ReviewCommentEntity comment = ReviewCommentEntity.builder()
                .rvcContent(request.getContent())
                .rvcStatus(request.getStatus())
                .rvcCreate(LocalDate.now())
                .company(company)
                .reviewId(request.getReviewId())
                .build();

        return comment;

    }

    public ReviewCommentResponse toResponse(ReviewCommentEntity entity) {
        return ReviewCommentResponse.builder()
                .commentId(entity.getRvCommentId())
                .content(entity.getRvcContent())
                .status(entity.getRvcStatus())
                .cpId(entity.getCompany().getCpId())
                .reviewId(entity.getReviewId())
                .createdAt(entity.getRvcCreate())
                .build();
    }



}
