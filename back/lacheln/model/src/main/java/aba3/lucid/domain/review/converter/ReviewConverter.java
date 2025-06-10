package aba3.lucid.domain.review.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import aba3.lucid.domain.review.converter.ReviewImageConverter;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class ReviewConverter {

    private final ReviewImageConverter reviewImageConverter;


    public ReviewResponse toResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .nickname(review.getUser().getUserNickName())
                .content(review.getRvContent())
                .score(review.getRvScore())
                .companyId(review.getCompanyId())
                .productId(review.getProductId())
                .productName(review.getProductName())
                .imageUrlList(reviewImageConverter.toResponseList(review.getImageList()))
                .createdAt(review.getRvCreate())
                .build()
                ;
    }

    public List<ReviewResponse> toResponseList(List<ReviewEntity> reviewEntityList) {
        return reviewEntityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }
}
