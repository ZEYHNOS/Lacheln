package aba3.lucid.comment.controller;


import aba3.lucid.comment.business.ReviewCommentBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
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

    @GetMapping("/search/{reviewCommentId}")
    public API<ReviewCommentResponse> searchReviewComment(
            @PathVariable Long reviewCommentId
    ) {
        ReviewCommentResponse response = reviewCommentBusiness.getReviewComment(reviewCommentId);
        return API.OK(response);
    }

    @DeleteMapping("/delete/{reviewCommentId}")
    public API<Void> deleteReviewComment(
            @AuthenticationPrincipal CustomUserDetails company,
            @PathVariable Long reviewCommentId
    ) {
       reviewCommentBusiness.deleteReviewComment(reviewCommentId);
       return API.OK();
    }


}
