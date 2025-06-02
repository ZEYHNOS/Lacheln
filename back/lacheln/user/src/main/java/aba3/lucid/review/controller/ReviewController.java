package aba3.lucid.review.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.review.business.ReviewBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

}
