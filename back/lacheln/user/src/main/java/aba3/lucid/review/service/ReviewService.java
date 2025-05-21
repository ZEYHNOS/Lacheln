package aba3.lucid.review.service;

import aba3.lucid.domain.review.dto.*;
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

    @Transactional
    public void updateReview(String userId, Long reviewId, ReviewUpdateRequest request) {
        /*
         * [서비스 계층 역할]
         * - 컨트롤러로부터 받은 수정 요청을 비즈니스 계층으로 전달
         * - 트랜잭션은 @Transactional로 감싸 수정이 반영되도록 처리
         */
        reviewBusiness.updateReview(userId, reviewId, request);
    }

    /**
     * 리뷰 삭제 서비스
     * - 비즈니스 계층에 위임
     *
     * @param userId 현재 로그인한 사용자 UUID
     * @param reviewId 삭제할 리뷰 ID
     */
    @Transactional
    public void deleteReview(String userId, Long reviewId) {
        reviewBusiness.deleteReview(userId, reviewId);
    }

    /**
     * 리뷰 답글 작성 서비스
     *
     * - 해당 상품의 판매자(CompanyEntity)만 작성 가능
     * - 하나의 리뷰에 대해 한 업체당 하나의 답글만 작성 가능
     * - Controller로부터 전달받은 요청을 비즈니스 계층에 위임
     *
     * @param companyId 현재 로그인한 판매자의 ID
     * @param request 답글 작성 요청 DTO (리뷰 ID, 답글 내용 포함)
     */
    @Transactional
    public void createReviewComment(Long companyId, ReviewCommentCreateRequest request) {
        reviewBusiness.writeReviewComment(companyId, request);
    }

    /**
     * 리뷰 답글 수정 서비스
     *
     * - 답글을 작성한 판매자 본인만 수정 가능
     * - 삭제된 답글은 수정할 수 없음
     * - Controller로부터 전달받은 요청을 비즈니스 계층에 위임
     *
     * @param companyId 현재 로그인한 판매자의 ID
     * @param request 답글 수정 요청 DTO (답글 ID, 수정할 내용 포함)
     */
    @Transactional
    public void updateReviewComment(Long companyId, ReviewCommentUpdateRequest request) {
        reviewBusiness.updateReviewComment(companyId, request);
    }

    /**
     * 리뷰 답글 삭제 서비스
     *
     * - 작성자(판매자) 본인만 삭제할 수 있음
     * - 실제로 데이터를 삭제하는 것이 아니라 논리 삭제 처리
     * - 비즈니스 계층으로 삭제 요청을 위임함
     *
     * @param companyId 현재 로그인한 판매자 ID
     * @param commentId 삭제할 답글 ID
     */
    @Transactional
    public void deleteReviewComment(Long companyId, Long commentId) {
        reviewBusiness.deleteReviewComment(companyId, commentId);
    }
}
