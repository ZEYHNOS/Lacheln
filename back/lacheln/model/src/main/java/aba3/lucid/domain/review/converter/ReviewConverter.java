package aba3.lucid.domain.review.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import aba3.lucid.domain.review.converter.ReviewImageConverter;

@Converter
@RequiredArgsConstructor
public class ReviewConverter {

    private final ReviewImageConverter reviewImageConverter;


    public ReviewResponse toResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .nickname(review.getUser().getUserNickName())
                .content(review.getRvContent())
                .score(review.getRvScore())
                .imageUrlList(reviewImageConverter.toResponseList(review.getImageList()))
                .createdAt(review.getRvCreate())
                .build()
                ;
    }
}
