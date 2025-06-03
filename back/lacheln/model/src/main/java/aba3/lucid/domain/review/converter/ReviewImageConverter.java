package aba3.lucid.domain.review.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;

import java.util.List;

@Converter
public class ReviewImageConverter {

    public ReviewImageEntity toEntity(String imageUrl, ReviewEntity review) {
        return ReviewImageEntity.builder()
                .rvImageUrl(imageUrl)
                .review(review)
                .build()
                ;
    }


    public String toResponse(ReviewImageEntity entity) {
        return entity.getRvImageUrl();
    }


    public List<ReviewImageEntity> toEntityList(List<String> urlList, ReviewEntity review) {
        return urlList.stream()
                .map(it -> toEntity(it, review))
                .toList()
                ;
    }

    public List<String> toResponseList(List<ReviewImageEntity> reviewImageEntityList) {
        return reviewImageEntityList.stream()
                .map(ReviewImageEntity::getRvImageUrl)
                .toList()
                ;
    }

}
