package aba3.lucid.review.controller;

import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.review.service.ReviewService;
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

import java.util.List;

/**
 * 리뷰 조회 컨트롤러
 * - 회원 / 비회원 누구나 접근 가능한 리뷰 조회 API
 */
@Slf4j
@RestController
@RequestMapping("/api/products") // /api/products/{productId}/reviews
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "리뷰 컨트롤러")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 상품 ID로 리뷰 목록 조회
     *
     * @param productId 리뷰를 조회할 상품의 ID
     * @return List<ReviewResponse> 리뷰 응답 DTO 리스트
     */
    @GetMapping("/{productId}/reviews")
    @Operation(
            summary = "상품별 리뷰 목록 조회",
            description = "특정 상품에 작성된 리뷰들을 조회합니다. 회원/비회원 모두 조회 가능합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 상품에 대한 리뷰가 없습니다.")
    })
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
            @Parameter(description = "조회할 상품 ID", example = "1")
            @PathVariable Long productId
    ) {
        List<ReviewResponse> response = reviewService.getReviewsByProductId(productId);

        return ResponseEntity.ok(response);
    }

    /**
     * [POST] 리뷰 작성 API
     *
     * 실구매자(해당 상품을 결제한 사용자)만 리뷰를 작성할 수 있습니다.
     * 리뷰 작성 시 평점, 내용, 이미지 URL 리스트를 전달받습니다.
     *
     * @param userId 사용자 UUID (임시로 Header로 받음)
     * @param request 리뷰 작성 요청 DTO
     * @return HTTP 200 OK
     */
    @PostMapping
    @Operation(
            summary = "리뷰 작성",
            description = "상품을 실제로 구매한 사용자만 리뷰를 작성할 수 있습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패"),
            @ApiResponse(responseCode = "403", description = "리뷰 작성 권한 없음 (결제자가 아님)"),
            @ApiResponse(responseCode = "404", description = "결제 정보 없음")
    })
    public ResponseEntity<Void> createReview(
            @Parameter(description = "로그인한 사용자 UUID", example = "50bc5f68-2af2-11f0-bd1c-6b3a44583745")
            @RequestHeader("X-USER-ID") String userId,

            @Valid @RequestBody ReviewCreateRequest request
    ) {
        // 서비스 호출을 통해 리뷰 저장 처리
        reviewService.createReview(userId, request);

        // 성공 시 200 OK 반환 (추후 생성된 리뷰 ID 반환도 가능)
        return ResponseEntity.ok().build();
    }
}
