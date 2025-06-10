package aba3.lucid.review.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.review.dto.ReviewSearchRequest;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.review.business.ReviewBusiness;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "리뷰 컨트롤러")
public class ReviewController {

    private final ReviewBusiness reviewBusiness;

    @PostMapping("/write")
    public API<ReviewResponse> writeReview(
            @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
        ) {
        ReviewResponse response = reviewBusiness.writeReview(request, user.getUserId());

        return API.OK(response);
    }

    @DeleteMapping("/{reviewId}")
    public API<String> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        reviewBusiness.deleteReview(user.getUserId(), reviewId);
        return API.OK("삭제 완료");
    }

    @GetMapping("/search")
    public API<List<ReviewResponse>> reviewSearch(
            ReviewSearchRequest request
    ) {
//        List<ReviewResponse> response = reviewBusiness.search(request);

//        return API.OK(response);
        return null;
    }

    // 유저 리뷰 불러오기
    @GetMapping("/user")
    public API<List<ReviewResponse>> getUserReview(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<ReviewResponse> reviewResponseList = reviewBusiness.getUserReviewList(user.getUserId());
        return API.OK(reviewResponseList);
    }

    // 업체 리뷰 불러오기

    // 상품 리뷰 불러오기



}
