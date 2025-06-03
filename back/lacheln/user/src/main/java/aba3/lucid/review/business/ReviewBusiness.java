package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.review.converter.ReviewConverter;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Business
@RequiredArgsConstructor
public class ReviewBusiness {

    private final ReviewService reviewService;
    private final ReviewConverter reviewConvertor;

    public ReviewResponse writeReview(ReviewCreateRequest request, String userId) {
        ReviewEntity review = reviewService.findByIdWithThrow(request.getReviewId());

        ReviewEntity writeReview = reviewService.writeReview(review ,request, userId);

        return reviewConvertor.toResponse(writeReview);
    }

    public void deleteReview(String userId, Long reviewId) {
        ReviewEntity review = reviewService.findByIdWithThrow(reviewId);

        reviewService.delete(userId, review);

    }
}