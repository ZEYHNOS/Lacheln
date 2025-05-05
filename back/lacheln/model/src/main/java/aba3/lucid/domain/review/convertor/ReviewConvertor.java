package aba3.lucid.domain.review.convertor;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewConvertor {

    /**
     * 리뷰 작성 요청을 ReviewEntity로 변환
     *
     * @param payDetail 결제 상세 정보 (상품 정보 포함)
     * @param user 사용자 (작성자)
     * @param request 리뷰 작성 요청 DTO
     * @return ReviewEntity
     */
    public static ReviewEntity toReviewEntity(PayDetailEntity payDetail, UsersEntity user, ReviewCreateRequest request) {
        PayManagementEntity payManagement = payDetail.getPayManagement();
        ProductEntity product = null; // 상품 정보가 PayDetail에 없다면, 외부에서 따로 세팅 필요

        return ReviewEntity.builder()
                .payManagement(payManagement)
                .product(product)  // 필요시 null → 이후 로직에서 set 가능
                .user(user)
                .rvContent(request.getRvContent())
                .rvCreate(LocalDateTime.now())
                .rvStatus("REGISTERED")
                .rvScore(request.getRvScore())
                .imageList(null) // 리뷰 이미지 리스트는 나중에 별도로 저장
                .build();
    }

    /**
     * 이미지 URL 리스트를 ReviewImageEntity 리스트로 변환
     *
     * @param review 저장된 리뷰 엔티티
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
}
