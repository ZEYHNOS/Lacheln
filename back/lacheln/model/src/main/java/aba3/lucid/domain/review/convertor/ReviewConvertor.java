package aba3.lucid.domain.review.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.CommentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ReviewConvertor {

    /**
     * 리뷰 작성 요청을 ReviewEntity로 변환합니다.
     *
     * @param payManagement 결제 관리 정보
     * @param payDetail 결제 상세 정보
     * @param product 상품 정보
     * @param user 사용자 정보 (작성자)
     * @param request 리뷰 작성 요청 DTO
     * @return ReviewEntity
     */
    public static ReviewEntity toReviewEntity(
            PayManagementEntity payManagement,
            PayDetailEntity payDetail,
            ProductEntity product,
            UsersEntity user,
            ReviewCreateRequest request
    ) {
        return ReviewEntity.builder()
                .payDetailEntity(payDetail)
                .user(user)
                .rvContent(request.getRvContent())
                .rvCreate(LocalDateTime.now())
                .rvStatus(ReviewStatus.REPLY_NEEDED)
                .rvScore(request.getRvScore())
                .imageList(null)
                .build();
    }

    /**
     * 이미지 URL 리스트를 ReviewImageEntity 리스트로 변환합니다.
     *
     * @param review 리뷰 엔티티
     * @param imageUrls 이미지 URL 문자열 리스트
     * @return ReviewImageEntity 리스트
     */
    public static List<ReviewImageEntity> toReviewImageEntities(ReviewEntity review, List<String> imageUrls) {
        return imageUrls.stream()
                .map(url -> ReviewImageEntity.builder()
                        .review(review)
                        .rvImageUrl(url)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * ReviewEntity를 사용자에게 보여줄 응답 DTO로 변환합니다.
     *
     * @param review ReviewEntity 객체
     * @return ReviewResponse DTO
     */
    public static ReviewResponse toReviewResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .nickname(review.getUser().getUserNickName())
                .content(review.getRvContent())
                .score(review.getRvScore())
                .imageUrls(
                        review.getImageList() != null
                                ? review.getImageList().stream()
                                .map(ReviewImageEntity::getRvImageUrl)
                                .toList()
                                : List.of()
                )
                .createdAt(review.getRvCreate())
                .build();
    }
}
