package aba3.lucid.review.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.repository.PayDetailRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.repository.ProductRepository;
import aba3.lucid.domain.review.convertor.ReviewConvertor;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewImageRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final PayDetailRepository payDetailRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    /**
     * 리뷰 저장 처리 (트랜잭션 포함)
     * - 결제 상세 정보 검증 및 상품 정보 연계
     * - 작성자 유효성 검증
     * - 리뷰 + 이미지 저장
     */
    @Transactional
    public void saveReviewWithImages(String userId, ReviewCreateRequest request) {

        // 1. 결제 상세 정보 조회
        PayDetailEntity payDetail = payDetailRepository.findById(Long.valueOf(request.getPayId()))
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 결제 상세 정보를 찾을 수 없습니다."));

        // 2. 사용자 정보 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        // 3. 본인 결제 여부 확인
        if (!payDetail.getPayManagement().getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "본인의 결제 건에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        // 4. 상품 정보 조회 및 연계
        ProductEntity product = productRepository.findById(payDetail.getPdId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        // 5. Convertor를 통한 ReviewEntity 생성
        ReviewEntity review = ReviewConvertor.toReviewEntity(payDetail, user, request);
        review.setProduct(product); // 상품 직접 연계

        // 6. 리뷰 저장
        reviewRepository.save(review);

        // 7. 이미지 저장 (선택 사항)
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ReviewImageEntity> imageEntities = ReviewConvertor.toReviewImageEntities(review, request.getImageUrls());
            reviewImageRepository.saveAll(imageEntities);
        }
    }

    /**
     * 리뷰 삭제 처리 (Soft Delete)
     * - 작성자 본인만 삭제 가능
     */
    @Transactional
    public void softDeleteReview(String userId, Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "본인의 리뷰만 삭제할 수 있습니다.");
        }

        review.markAsDeleted();
    }
}
