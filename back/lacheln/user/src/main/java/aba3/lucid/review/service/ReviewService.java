package aba3.lucid.review.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.enums.PaymentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.converter.ReviewImageConverter;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.repository.ReviewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.payment.service.PayDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PayDetailService payDetailService;
    private final ReviewRepository reviewRepository;

    private final ReviewImageConverter reviewImageConverter;

    private final RabbitTemplate rabbitTemplate;

    // 리뷰 생성(리뷰 작성 상태 X)
    public void create(List<PayDetailEntity> payDetailEntityList) {
        List<ReviewEntity> reviewEntityList = new ArrayList<>();

        payDetailEntityList.forEach(
                it -> {
                    reviewEntityList.add(ReviewEntity.builder()
                            .productId(it.getPdId())
                            .companyId(it.getCpId())
                            .payDetailEntity(it)
                            .rvStatus(ReviewStatus.REPLY_NEEDED)
                            .build())
                    ;

                    rabbitTemplate.convertAndSend("", "", "");
                }
        );

        reviewRepository.saveAll(reviewEntityList);
    }

    // 리뷰 작성(사용자가 리뷰 작성하기) -> 답글 생성하기
    public ReviewEntity writeReview(ReviewEntity review, ReviewCreateRequest request, String userId) {
        if (!review.getRvStatus().equals(ReviewStatus.REPLY_NEEDED)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // UserId 검사 TODO 리팩토링
        if (!review.getPayDetailEntity().getPayManagement().getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        // 결제된 상태가 아닐 때(취소, 환불 등등)
        if (!review.getPayDetailEntity().getPayManagement().getPayStatus().equals(PaymentStatus.PAID)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        review.updateField(request);
        review.updateImageList(reviewImageConverter.toEntityList(request.getImageUrlList(), review));

        // 업체 답글 생성
        ReviewCommentEventDto dto = new ReviewCommentEventDto(review.getReviewId(), review.getPayDetailEntity().getCpId());
        rabbitTemplate.convertAndSend("review.comment.exchange", "review.comment.queue", dto);

        return reviewRepository.save(review);
    }

    // 리뷰 삭제(리뷰 삭제하기)
    @Transactional
    public void delete(String userId, ReviewEntity review) {
        if (!review.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
        reviewRepository.delete(review);

        // 답글 삭제 요청 보내기
        rabbitTemplate.convertAndSend("review.comment.exchange", "comment.delete.queue", review.getReviewId());
    }

    public List<ReviewEntity> getReviewEntityListByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    public List<ReviewEntity> getReviewEntityListByCompanyId(Long companyId) {
        return reviewRepository.findAllByCompanyId(companyId);
    }

    // TODO 나중에 리팩토링(Join 너무 많음)
    public List<ReviewEntity> getReviewEntityListByUser(UsersEntity user) {
        return reviewRepository.findAllByPayDetailEntity_PayManagement_User(user);
    }


    public ReviewEntity findByIdWithThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}