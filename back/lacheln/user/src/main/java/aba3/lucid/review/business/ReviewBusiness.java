package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.review.convertor.ReviewConvertor;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
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

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        /*
         * [1] 리뷰 엔티티 목록 조회
         * - ReviewRepository에서 productId를 기준으로 리뷰 리스트를 가져온다.
         * - ReviewEntity는 상품(ProductEntity)과 연관관계를 가지고 있으므로, product.productId로 조회 가능
         */
        List<ReviewEntity> reviewList = reviewRepository.findByProductPdId(productId);

        /*
         * [2] 리뷰 응답 DTO로 변환
         * - 사용자에게 보여줄 정보만 골라서 담은 ReviewResponse로 변환한다.
         * - 변환은 ReviewConvertor의 toReviewResponse 메서드에서 처리
         */
        return reviewList.stream()
                .map(ReviewConvertor::toReviewResponse) // ReviewEntity → ReviewResponse
                .toList(); // 변환된 리스트를 반환
    }
}
