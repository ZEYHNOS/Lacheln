package aba3.lucid.comment.service;


import aba3.lucid.alert.business.CompanyAlertBusiness;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.alert.dto.CompanyAlertDto;
import aba3.lucid.domain.alert.dto.MutualAlert;
import aba3.lucid.domain.alert.dto.UserAlertDto;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewCommentRepository reviewCommentRepository;
    private final CompanyService companyService;
    private final RabbitTemplate rabbitTemplate;
    private final CompanyAlertBusiness companyAlertBusiness;

    public ReviewCommentEntity findByIdWithThrow(Long reviewCommentId) {
        return reviewCommentRepository.findById(reviewCommentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    public ReviewCommentEntity findByReviewIdWithThrow(Long reviewId) {
        return reviewCommentRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void deleteReviewComment(ReviewCommentEntity entity) {
        entity.deleteRequest();
        reviewCommentRepository.save(entity);
    }

    @Transactional
    public void deleteByReviewId(Long reviewId) {
        reviewCommentRepository.deleteByReviewId(reviewId);
    }

    public ReviewCommentEntity findByReviewId(Long reviewId) {
        return reviewCommentRepository.findByReviewId(reviewId).orElse(null);
    }

    public ReviewCommentEntity findByCommentId(Long commentId) {
        return reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "답글을 찾을 수 없습니다."));
    }

    // 삭제되지 않은 리뷰 답글 반환하기
    public List<ReviewCommentEntity> findByReviewIdList(List<Long> reviewIdList) {
        return reviewCommentRepository.findAllByReviewIdIn(reviewIdList).stream()
                .filter(this::isNotDeleted)
                .toList()
                ;
    }

    // 리뷰 답글 작성
    public ReviewCommentEntity writeReviewComment(ReviewCommentEntity reviewComment, ReviewCommentRequest request) {
        reviewComment.writeReviewComment(request);
        ReviewCommentEntity savedReviewCommentEntity = reviewCommentRepository.save(reviewComment);
        // 알림 보내기
        rabbitTemplate.convertAndSend("user.exchange", "to.user", UserAlertDto.reviewCommentAlert(request.getUserId()));
        return savedReviewCommentEntity;
    }

    public void initBaseReviewComment(ReviewCommentEntity reviewCommentEntity) {
        CompanyAlertDto dto = CompanyAlertDto.reviewWrite(reviewCommentEntity);
        companyAlertBusiness.alertRegister(dto, reviewCommentEntity.getCompany().getCpId());
        reviewCommentRepository.save(reviewCommentEntity);
    }


    private boolean isNotDeleted(ReviewCommentEntity entity) {
        return !entity.getRvcStatus().equals(ReviewStatus.DELETED);
    }

}
