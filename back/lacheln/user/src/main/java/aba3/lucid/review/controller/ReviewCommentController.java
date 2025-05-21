package aba3.lucid.review.controller;


import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.review.business.ReviewCommentBusiness;
import aba3.lucid.review.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentBusiness reviewCommentBusiness;
    private final ReviewCommentService reviewCommentService;

    @GetMapping("/search/{reviewCommentId}")
    public API<ReviewCommentResponse> searchReviewComment(
            @AuthenticationPrincipal CustomUserDetails company,
            @PathVariable long reviewCommentId,
            @RequestBody ReviewCommentRequest reviewCommentRequest
    ) {
        ReviewCommentResponse response = reviewCommentBusiness.getReviewComment(reviewCommentId);
        return API.OK(response);
    }

    @DeleteMapping("/delete/{reviewCommentId}")
    public API<ReviewCommentResponse> deleteReviewComment(
            @AuthenticationPrincipal CustomUserDetails company,
            @PathVariable long reviewCommentId,
            @RequestBody ReviewCommentRequest reviewCommentRequest
    ) {
        ReviewCommentResponse response = reviewCommentBusiness.getReviewComment(reviewCommentId);
        return API.OK(response);
    }


}
