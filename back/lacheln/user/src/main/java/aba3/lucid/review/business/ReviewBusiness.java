package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.review.converter.ReviewConverter;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.dto.ReviewUpdateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.review.service.ReviewService;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ReviewBusiness {

    private final UserService userService;
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

    public List<ReviewResponse> getUserReviewList(String userId) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        List<ReviewEntity> reviewEntityList = reviewService.getReviewEntityListByUser(user);
        return reviewConvertor.toResponseList(reviewEntityList);
    }

    public List<ReviewResponse> getCompanyReviewList(Long id) {
        List<ReviewEntity> reviewEntityList = reviewService.getReviewEntityListByCompanyId(id);
        return reviewConvertor.toResponseList(reviewEntityList);
    }

    public List<ReviewResponse> getProductReviewList(Long productId) {
        List<ReviewEntity> reviewEntityList = reviewService.getReviewEntityListByProductId(productId);
        return reviewConvertor.toResponseList(reviewEntityList);
    }

    public ReviewResponse updateReview(ReviewUpdateRequest request, String userId, Long reviewId) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        ReviewEntity review = reviewService.findByIdWithThrow(reviewId);

        ReviewEntity updatedReview = reviewService.updateReview(user, review, request);
        return reviewConvertor.toResponse(updatedReview);
    }
}