package aba3.lucid.review.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.converter.ReviewImageConverter;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewUpdateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewImageRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    private final ReviewImageConverter reviewImageConverter;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    // 리뷰 생성(리뷰 작성 상태 X)
    public void initBaseReview(List<PayDetailEntity> payDetailEntityList) {
        reviewRepository.saveAll(payDetailEntityList.stream().map(this::createBaseReview).toList());
    }

    // 리뷰 작성(사용자가 리뷰 작성하기) -> 답글 생성하기
    public ReviewEntity writeReview(ReviewEntity review, ReviewCreateRequest request, UsersEntity user) {
        // 리뷰 작성 전 검증 로직
        verifyWriteReview(user, review);

        reviewImageRepository.deleteAll(review.getImageList());

        review.updateField(request);
        review.updateImageList(reviewImageConverter.toEntityList(request.getImageUrlList(), review));


        ReviewCommentEventDto dto = ReviewCommentEventDto.createBaseReviewCommentRequest(review.getCompanyId(), review.getReviewId(), user.getUserId());
        rabbitTemplate.convertAndSend("review.comment.exchange", "review.comment.queue", dto);

        return reviewRepository.save(review);
    }

    @Transactional
    public ReviewEntity updateReview(UsersEntity user, ReviewEntity review, ReviewUpdateRequest request) {
        verifyUpdateReview(user, review);

        log.info("request : {}", request);

        List<ReviewImageEntity> reviewImageEntityList = reviewImageConverter.toEntityList(request.getReviewImageUrl(), review);

        review.updateField(request);
        review.updateImageList(reviewImageEntityList);
        return review;
    }

    // 리뷰 삭제(리뷰 삭제하기)
    @Transactional
    public void delete(UsersEntity user, ReviewEntity review) {
        throwIfNotOwnerWriting(user, review);
        review.deleteRequest();

        reviewImageRepository.deleteAllByReview_ReviewId(review.getReviewId());
        
        // 답글 삭제 요청 보내기
        ReviewCommentEventDto dto = ReviewCommentEventDto.deleteRequest(review.getReviewId());
        rabbitTemplate.convertAndSend("comment.delete.exchange", "comment.delete.queue", dto);
    }

    // 상품에 등록된 리뷰 조회(REGISTERED, UPDATED 상태만)
    public List<ReviewEntity> getReviewEntityListByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId).stream()
                .filter(this::isRegistered)
                .toList()
                ;
    }

    // 업체에 등록된 리뷰 조회(REGISTERED, UPDATED 상태만)
    public List<ReviewEntity> getReviewEntityListByCompanyId(Long companyId) {
        return reviewRepository.findAllByCompanyId(companyId).stream()
                .filter(this::isRegistered)
                .toList()
                ;
    }

    // TODO 나중에 리팩토링(Join 너무 많음)
    // 유저 리뷰 조회
    public List<ReviewEntity> getReviewEntityListByUser(UsersEntity user) {
        return reviewRepository.findAllByUser_UserId(user.getUserId());
    }


    public ReviewEntity findByIdWithThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }


    @Transactional
    private ReviewEntity createBaseReview(PayDetailEntity payDetail) {
        return ReviewEntity.builder()
                .productId(payDetail.getPdId())
                .user(payDetail.getPayManagement().getUser())
                .productName(payDetail.getProductName())
                .companyId(payDetail.getCpId())
                .payDetailEntity(payDetail)
                .rvStatus(ReviewStatus.REPLY_NEEDED)
                .build()
                ;
    }

    // 등록, 수정 상태 리뷰만 true
    private boolean isRegistered(ReviewEntity review) {
        return review.getRvStatus().equals(ReviewStatus.REGISTERED)
                || review.getRvStatus().equals(ReviewStatus.UPDATED);
    }


    // 리뷰 작성 전 검증
    private void verifyWriteReview(UsersEntity user, ReviewEntity review) {
        throwIfNotStatusReplyNeeded(review); // 리뷰 작성 필요 상태가 아닐 경우
        throwIfNotOwnerWriting(user, review); // 본인이 쓴 글이 아닐 경우
        throwIfNotPaid(review); // 결제된 상태가 아닐 때(취소, 환불 등등)
    }

    // 리뷰 수정 전 검증
    private void verifyUpdateReview(UsersEntity user, ReviewEntity review) {
        throwIfNotOwnerWriting(user, review); // 리뷰 작성 본인 검사
        throwIfStatusDeleteOrReplyNeeded(review); // 삭제하거나 리뷰 작성 필요(잘못된 요청) 상태일 때
    }

    // ReplyNeeded 상태가 아닐 때
    private void throwIfNotStatusReplyNeeded(ReviewEntity review) {
        if (!review.getRvStatus().equals(ReviewStatus.REPLY_NEEDED)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    private void throwIfStatusDeleteOrReplyNeeded(ReviewEntity review) {
        // 삭제 404
        if (review.getRvStatus().equals(ReviewStatus.DELETED)) {
            throw new ApiException(ErrorCode.NOT_FOUND);
        }

        // 잘못된 요청
        if (review.getRvStatus().equals(ReviewStatus.REPLY_NEEDED)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    // 결제된 상태가 아닐 때(취소, 환불 등등)
    private void throwIfNotPaid(ReviewEntity review) {
        if (!review.getPayDetailEntity().getPayManagement().getPayStatus().equals(PaymentStatus.PAID)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

    // 리뷰 작성자가 아닐 경우
    private void throwIfNotOwnerWriting(UsersEntity user, ReviewEntity review) {
        if (!review.getUser().equals(user)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }

}