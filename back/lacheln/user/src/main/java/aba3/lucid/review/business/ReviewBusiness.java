package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.review.converter.ReviewConvertor;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ReviewBusiness {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성 처리
     * - 결제 정보, 사용자 정보, 상품 정보 확인
     * - 리뷰 엔티티 및 이미지 엔티티 생성
     * - 서비스 계층에 위임하여 저장 (트랜잭션 처리)
     */
    public void writeReview(String userId, ReviewCreateRequest request) {
        // 결제 관리 + 사용자 정보 조회 및 검증
        PayManagementEntity pay = reviewService.getPayManagementIfValid(userId, request.getPayId());

        // 결제 상세 정보 추출 (첫 번째 항목 사용)
        PayDetailEntity payDetail = reviewService.getFirstPayDetailOrThrow(pay);

        // 상품 정보 조회
        ProductEntity product = reviewService.getProductFromPayDetail(payDetail);

        // 사용자 조회
        UsersEntity user = pay.getUser();

        // 리뷰 생성
        ReviewEntity review = ReviewConvertor.toReviewEntity(pay, payDetail, product, user, request);

        // 이미지가 있다면 이미지 엔티티 리스트 생성
        List<ReviewImageEntity> imageList = ReviewConvertor.toReviewImageEntities(review, request.getImageUrls());

        // 트랜잭션 처리 위임
        reviewService.createReview(review, imageList);
    }

    /**
     * 리뷰 삭제 처리
     * - 작성자 본인만 삭제 가능
     * - Soft Delete 처리 후 서비스 계층에서 반영
     */
    public void deleteReview(String userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "해당 리뷰를 삭제할 권한이 없습니다.");
        }

        review.markAsDeleted(); // 엔티티 상태 변경

        reviewService.deleteReview(review); // 트랜잭션 처리
    }
}