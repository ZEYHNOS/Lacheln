package aba3.lucid.review.controller;

import aba3.lucid.domain.review.dto.ReviewCreateRequest;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "리뷰 컨트롤러")
public class ReviewController {

    private final ReviewBusiness reviewBusiness;

    /**
     * [POST] 리뷰 작성 API
     * - 실제 구매한 사용자만 작성 가능
     * - 이미지 URL 포함 가능
     */
    @PostMapping("/reviews")
    @Operation(
            summary = "리뷰 작성",
            description = "상품을 실제로 구매한 사용자만 리뷰를 작성할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패"),
            @ApiResponse(responseCode = "403", description = "작성 권한 없음 (결제자 아님)"),
            @ApiResponse(responseCode = "404", description = "결제 정보 없음")
    })
    public ResponseEntity<Void> createReview(
            @Parameter(description = "로그인한 사용자 UUID", example = "50bc5f68-2af2-11f0-bd1c-6b3a44583745")
            @RequestHeader("X-USER-ID") String userId,

            @Valid @RequestBody ReviewCreateRequest request
    ) {
        reviewBusiness.writeReview(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * [DELETE] 리뷰 삭제 API
     * - 작성자 본인만 삭제 가능 (Soft Delete)
     */
    @DeleteMapping("/reviews/{reviewId}")
    @Operation(
            summary = "리뷰 삭제",
            description = "작성자 본인만 리뷰를 삭제할 수 있습니다. 삭제된 리뷰는 화면에 표시되지 않습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "리뷰 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 리뷰 없음")
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "로그인한 사용자 UUID", example = "50bc5f68-2af2-11f0-bd1c-6b3a44583745")
            @RequestHeader("X-USER-ID") String userId,

            @Parameter(description = "삭제할 리뷰 ID", example = "123")
            @PathVariable Long reviewId
    ) {
        reviewBusiness.deleteReview(userId, reviewId);
        return ResponseEntity.ok().build();
    }
}
