package aba3.lucid.review.business;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.repository.PayDetailRepository;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.review.convertor.ReviewCommentConvertor;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import aba3.lucid.domain.review.dto.ReviewCommentResponse;
import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.repository.ReviewCommentRepository;
import aba3.lucid.review.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewCommentBusiness {
    private final PayManagementRepository payRepository;
    private final PayDetailRepository payDetailRepository;
    private final ReviewCommentConvertor reviewCommentConvertor;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewCommentService reviewCommentService;

    /**
     * 리뷰가 실제 존재하는지
     *  이 실제로 리뷰대상(상품・서비스를 제공한) 업체인지 검증
     * 그 회사와 로그인된 회사가 같은지
    */

    public void validateCanReply(ReviewEntity review, CompanyEntity company) throws AccessDeniedException {
        Validator.throwIfNull(review, "리뷰가 없습니다");
        Validator.throwIfNull(company,  "업체 정보가 없습니다");

        PayDetailEntity detail = review.getPayDetailEntity();
        if(detail == null) {
            throw new IllegalStateException("리뷰에 연결된 결제 상세 정보가 없습니다");
        }


        long targetCompany = detail.getCpId();
        //작성 권한(업체 일치) 확인
        if(targetCompany != company.getCpId()) {
            throw  new AccessDeniedException("본인 업체에 달린 리뷰에만 답글을 작성할 수 있습니다");
        }

    }

    public ReviewCommentResponse addComment(ReviewEntity review, CompanyEntity company, ReviewCommentRequest request) throws AccessDeniedException {
        Validator.throwIfNull(review);
        Validator.throwIfNull(company);

        validateCanReply(review,company);

        ReviewCommentEntity entity = reviewCommentConvertor.toEntity(
               company, review, request
        );
        ReviewCommentEntity savedEntity = reviewCommentService.addComment(entity);
        return reviewCommentConvertor.toResponse(savedEntity);

    }

    public void deleteReviewComment(long reviewCommentId) {
        ReviewCommentEntity reviewComment = reviewCommentRepository.findById(reviewCommentId).orElse(null);
        reviewCommentService.deleteComment(reviewComment);
    }

    public ReviewCommentResponse getReviewComment(long reviewCommentId) {
        Optional<ReviewCommentEntity> commentOpt = reviewCommentRepository.findById(reviewCommentId);
        if(commentOpt.isPresent()) {
            ReviewCommentEntity reviewComment = commentOpt.get();
            return reviewCommentConvertor.toResponse(reviewComment);
        }else {
            throw  new ApiException(ErrorCode.NOT_FOUND, " 답글을 찾을 수 없습니다");
        }
    }

}
