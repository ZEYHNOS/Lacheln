package aba3.lucid.comment.business;


import aba3.lucid.comment.service.ReviewCommentService;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.review.converter.ReviewCommentConverter;
import aba3.lucid.domain.review.dto.ReviewCommentEventDto;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewCommentBusiness {


    private final ReviewCommentConverter reviewCommentConverter;
    private final ReviewCommentService reviewCommentService;
    private final CompanyService companyService;


    // ReviewComment Base 생성하기(내용, 생성일 Null)
    public void initBaseReviewComment(ReviewCommentEventDto eventDto) {
        CompanyEntity company = companyService.findByIdWithThrow(eventDto.getCpId());
        ReviewCommentEntity reviewCommentEntity = reviewCommentConverter.toEntity(eventDto, company);
        reviewCommentService.initBaseReviewComment(reviewCommentEntity);
    }

    // 리뷰 답글 작성
    @Transactional
    public ReviewCommentResponse writeReviewComment(Long rvCommentId, ReviewCommentRequest request, Long cpId) {
        ReviewCommentEntity reviewComment = reviewCommentService.findByIdWithThrow(rvCommentId);
        CompanyEntity company = companyService.findByIdWithThrow(cpId);

        if (!company.equals(reviewComment.getCompany())) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        ReviewCommentEntity savedReviewComment = reviewCommentService.writeReviewComment(reviewComment, request);
        return reviewCommentConverter.toResponse(savedReviewComment);
    }

    // 리뷰 답글 삭제
    public void deleteReviewComment(Long reviewCommentId) {
        ReviewCommentEntity reviewComment = reviewCommentService.findByReviewId(reviewCommentId);
        reviewCommentService.deleteReviewComment(reviewComment);
    }

    // 리뷰가 삭제 되었을 때 삭제
    public void deleteReviewCommentByReviewId(Long reviewId) {
        reviewCommentService.deleteByReviewId(reviewId);
    }

    // 리뷰 ID를 통해 리뷰 답글 조회하기
    public ReviewCommentResponse findByReviewId(Long reviewId) {
        ReviewCommentEntity entity = reviewCommentService.findByReviewId(reviewId);
        return reviewCommentConverter.toResponse(entity);
    }

    // 리뷰 ID 리스트를 통해 리뷰 답글들을 조회하기
    public List<ReviewCommentResponse> findByReviewIdList(List<Long> reviewIdList) {
        List<ReviewCommentEntity> reviewCommentEntityList = reviewCommentService.findByReviewIdList(reviewIdList);
        return reviewCommentConverter.toResponseList(reviewCommentEntityList);
    }

}
