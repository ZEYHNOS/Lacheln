package aba3.lucid.comment.controller;


import aba3.lucid.comment.business.ReviewCommentBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.domain.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentBusiness reviewCommentBusiness;

    @GetMapping("/{reviewId}")
    public API<ReviewCommentResponse> getReviewCommentByReview(
            @PathVariable Long reviewId
    ) {
        ReviewCommentResponse response = reviewCommentBusiness.findByReviewId(reviewId);
        return API.OK(response);
    }


    @PostMapping("/list")
    public API<List<ReviewCommentResponse>> getReviewCommentByReviewList(
            @RequestBody List<Long> reviewIdList
    ) {
        List<ReviewCommentResponse> responseList = reviewCommentBusiness.findByReviewIdList(reviewIdList);
        return API.OK(responseList);
    }

    @PostMapping("/{reviewId}")
    public API<ReviewCommentResponse> writeReviewComment(
            @PathVariable Long reviewId,
            @RequestBody ReviewCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ReviewCommentResponse response = reviewCommentBusiness.writeReviewComment(reviewId, request, user.getCompanyId());
        return API.OK(response);
    }

    @DeleteMapping("/{rvcId}")
    public API<String> deleteReviewComment(
            @PathVariable Long rvcId
    ) {
        reviewCommentBusiness.deleteReviewComment(rvcId);
        return API.OK("삭제 되었습니다.");
    }

}
