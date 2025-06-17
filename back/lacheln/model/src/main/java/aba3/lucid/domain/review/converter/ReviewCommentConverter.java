package aba3.lucid.domain.review.converter;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Converter
@RequiredArgsConstructor
public class ReviewCommentConverter {

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

    public ReviewCommentEntity toEntity(ReviewCommentEventDto dto, CompanyEntity company) {
        return ReviewCommentEntity.builder()
                .company(company)
                .reviewId(dto.getReviewId())
                .rvcStatus(ReviewStatus.REPLY_NEEDED)
                .build()
                ;
    }

    public List<ReviewCommentResponse> toResponseList(List<ReviewCommentEntity> reviewCommentEntityList) {
        if (reviewCommentEntityList == null || reviewCommentEntityList.isEmpty()) {
            return null;
        }

        return reviewCommentEntityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }
}
