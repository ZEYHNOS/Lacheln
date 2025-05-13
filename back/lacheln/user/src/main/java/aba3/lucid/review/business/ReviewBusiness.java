package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.review.convertor.ReviewConvertor;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.dto.ReviewUpdateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewImageRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ReviewBusiness {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final PayManagementRepository payManagementRepository;

    /**
     * 리뷰 작성 처리
     *
     * @param userId 현재 로그인한 사용자 ID (UUID 문자열)
     * @param request 리뷰 작성 요청 DTO
     */
    @Transactional
    public void writeReview(String userId, ReviewCreateRequest request) {

        // 1. 결제 내역 확인
        PayManagementEntity pay = payManagementRepository.findById(request.getPayId())
                .orElseThrow(() -> new IllegalArgumentException("해당 결제 정보를 찾을 수 없습니다."));

        UsersEntity user = pay.getUser();
        if (!user.getUserId().equals(userId)) {
            throw new IllegalStateException("본인이 결제한 상품에만 리뷰 작성이 가능합니다.");
        }

        // 2. 결제 상세 엔티티 선택 (여기선 첫 번째 항목 사용)
        List<PayDetailEntity> payDetailList = pay.getPayDetailEntityList();
        if (payDetailList == null || payDetailList.isEmpty()) {
            throw new IllegalStateException("결제 상세 정보가 존재하지 않습니다.");
        }

        PayDetailEntity payDetail = payDetailList.get(0); // 또는 특정 조건으로 필터링

        // 3. ReviewEntity 생성 및 저장
        ReviewEntity review = ReviewConvertor.toReviewEntity(
                payDetail,
                user,
                request
        );
        reviewRepository.save(review);

        // 4. 리뷰 이미지 저장
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ReviewImageEntity> imageEntities =
                    ReviewConvertor.toReviewImageEntities(review, request.getImageUrls());
            reviewImageRepository.saveAll(imageEntities);
        }

        // 5. MQ 전송 (옵션)
        // rabbitTemplate.convertAndSend("exchange", "routing.key", payload);
    }

    /**
     * 상품 ID 기준으로 리뷰 목록 조회
     *
     * @param productId 상품 ID
     * @return ReviewResponse 리스트
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        List<ReviewEntity> reviewList = reviewRepository.findByProductPdId(productId);

        // 삭제된 리뷰("DELETED" 상태)는 제외하고 응답 리스트로 변환
        return reviewList.stream()
                .filter(review -> !"DELETED".equals(review.getRvStatus()))
                .map(ReviewConvertor::toReviewResponse)
                .toList();
    }

    /**
     * 리뷰 수정 처리
     *
     * @param userId 로그인한 사용자 ID (UUID)
     * @param reviewId 수정할 리뷰 ID
     * @param request 리뷰 수정 요청 DTO
     */
    @Transactional
    public void updateReview(String userId, Long reviewId, ReviewUpdateRequest request) {
        // 1. 리뷰 조회
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 2. 작성자 검증
        UsersEntity writer = review.getUser();
        if (!writer.getUserId().equals(userId)) {
            throw new IllegalStateException("해당 리뷰를 수정할 권한이 없습니다.");
        }

        // 3. 본문 및 평점 수정
        review.updateContentAndScore(request.getRvContent(), request.getRvScore());

        // 4. 기존 이미지 삭제 후 새 이미지 등록
        reviewImageRepository.deleteAll(review.getImageList());

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ReviewImageEntity> newImages =
                    ReviewConvertor.toReviewImageEntities(review, request.getImageUrls());
            reviewImageRepository.saveAll(newImages);
        }

        log.info("리뷰 수정 완료: reviewId={}, userId={}", reviewId, userId);
    }

    /**
     * 리뷰 삭제 처리
     *
     * - 작성자 본인만 삭제할 수 있음
     * - 삭제 시 리뷰 상태를 DELETED로 설정하고 삭제 시각을 기록
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @param reviewId 삭제할 리뷰 ID
     */
    @Transactional
    public void deleteReview(String userId, Long reviewId) {
        // 1. 리뷰 조회
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 2. 작성자 검증
        UsersEntity writer = review.getUser();
        if (!writer.getUserId().equals(userId)) {
            throw new IllegalStateException("해당 리뷰를 삭제할 권한이 없습니다.");
        }

        // 3. 논리 삭제 처리
        review.markAsDeleted(); // 상태를 DELETED로 바꾸고 삭제일시 기록

        log.info("리뷰 삭제 완료: reviewId={}, userId={}", reviewId, userId);
    }
}
