package aba3.lucid.domain.review.convertor;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.review.dto.ReviewResponse;
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

    public static ReviewResponse toReviewResponse(ReviewEntity review) {
        /*
         * ReviewEntity → ReviewResponse 변환 메서드
         * - 사용자에게 보여줄 데이터만 포함된 DTO를 생성한다.
         * - 작성자 닉네임, 리뷰 내용, 평점, 이미지 URL 리스트, 작성일시를 포함한다.
         */
        return ReviewResponse.builder()
                .nickname(review.getUser().getUserNickName()) // 작성자 닉네임
                .content(review.getRvContent())               // 리뷰 본문 내용
                .score(review.getRvScore())                   // 리뷰 평점
                .imageUrls(
                        review.getImageList() != null             // 이미지가 존재하는 경우에만 변환
                                ? review.getImageList().stream()
                                .map(ReviewImageEntity::getRvImageUrl)
                                .toList()
                                : List.of()                           // 이미지가 없으면 빈 리스트 반환
                )
                .createdAt(review.getRvCreate())              // 작성일시
                .build();
    }
}
