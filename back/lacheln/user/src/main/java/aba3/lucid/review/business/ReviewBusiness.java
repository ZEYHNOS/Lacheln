package aba3.lucid.review.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.review.convertor.ReviewConvertor;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import aba3.lucid.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class ReviewBusiness {

    private final ReviewService reviewService;
    private final UsersRepository usersRepository;
    private final PayManagementRepository payManagementRepository;

    /**
     * 리뷰 작성 처리
     * - 사용자 검증 → 결제 검증 → 리뷰 Entity 변환 → 서비스 호출
     */
    public void createReview(String userId, ReviewCreateRequest request) {
        // 1. 사용자 존재 확인
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        // 2. 결제 내역 확인
        PayManagementEntity pay = payManagementRepository.findById(request.getPayId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 결제 정보를 찾을 수 없습니다."));

        // 3. 결제자 본인 확인
        if (!pay.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "본인이 결제한 상품에만 리뷰를 작성할 수 있습니다.");
        }

        // 4. 결제 상세 정보 확인
        List<PayDetailEntity> payDetailList = pay.getPayDetailEntityList();
        if (payDetailList == null || payDetailList.isEmpty()) {
            throw new ApiException(ErrorCode.NOT_FOUND, "결제 상세 정보가 존재하지 않습니다.");
        }

        PayDetailEntity payDetail = payDetailList.get(0); // 기본적으로 첫 항목 사용

        // 5. Convertor 통해 리뷰 Entity 변환
        ReviewEntity review = ReviewConvertor.toReviewEntity(payDetail, user, request);

        // 6. 저장 처리 위임
        reviewService.saveReviewWithImages(review, request.getImageUrls());
    }

    /**
     * 리뷰 삭제 처리
     * - 작성자 본인만 삭제 가능 (논리 삭제)
     */
    public void deleteReview(String userId, Long reviewId) {
        // 서비스에 위임 (작성자 확인 포함)
        reviewService.softDeleteReview(userId, reviewId);
    }
}
