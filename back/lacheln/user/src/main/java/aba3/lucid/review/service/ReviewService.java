package aba3.lucid.review.service;

import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.review.business.ReviewBusiness;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 리뷰 서비스 계층
 * - 트랜잭션 관리 및 비즈니스 호출을 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewBusiness reviewBusiness;

    /**
     * 리뷰 작성 서비스
     * @param userId 현재 로그인한 사용자 ID (UUID)
     * @param request 리뷰 작성 요청 DTO
     */
    @Transactional
    public void createReview(String userId, ReviewCreateRequest request) {
        reviewBusiness.writeReview(userId, request);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        /*
         * [중간 계층 역할]
         * - Controller에서 받은 요청을 ReviewBusiness로 전달
         * - 트랜잭션은 읽기 전용으로 설정 (@Transactional(readOnly = true))
         */
        return reviewBusiness.getReviewsByProductId(productId);
    }

    // 추후에 수정, 삭제, 조회 등의 기능도 이 안에 추가될 예정
}
