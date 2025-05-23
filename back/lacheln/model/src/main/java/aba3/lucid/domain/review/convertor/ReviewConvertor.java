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

/**
 * ReviewConvertor 클래스는
 * DTO(요청/응답 객체)와 Entity(실제 DB와 매핑되는 객체) 사이의 변환을 담당하는 도우미 클래스입니다.
 *
 * - Controller에서는 DTO를 받고,
 * - Service/Business에서는 Entity로 저장하거나 조회한 후,
 * - 다시 사용자에게 보여주기 위해 DTO로 바꿔주는 역할을 합니다.
 */
public class ReviewConvertor {

    /**
     * 리뷰 작성 요청(ReviewCreateRequest DTO)을 ReviewEntity로 변환합니다.
     *
     * @param payDetail 결제 상세 정보 (어떤 상품을 결제했는지 정보 포함)
     * @param user 리뷰를 작성한 사용자
     * @param request 사용자가 보낸 리뷰 작성 요청 내용 (본문, 평점, 이미지 등)
     * @return 실제 DB에 저장할 수 있는 ReviewEntity 객체
     */
    public static ReviewEntity toReviewEntity(PayDetailEntity payDetail, UsersEntity user, ReviewCreateRequest request) {
        PayManagementEntity payManagement = payDetail.getPayManagement(); // 결제 ID 기준 정보
        ProductEntity product = payDetail.getProduct(); // 구매한 상품 정보

        return ReviewEntity.builder()
                .payManagement(payManagement)     // 결제 관리 정보
                .payDetailEntity(payDetail)       // 결제 상세 정보
                .product(product)                 // 상품 정보
                .user(user)                       // 리뷰 작성자
                .rvContent(request.getRvContent()) // 리뷰 본문 내용
                .rvCreate(LocalDateTime.now())    // 작성 시간 (현재 시각으로 설정)
                .rvStatus("REGISTERED")           // 리뷰 상태 (기본값: 등록됨)
                .rvScore(request.getRvScore())    // 평점
                .build();
    }

    /**
     * 이미지 URL 리스트를 실제 DB에 저장 가능한 ReviewImageEntity 리스트로 변환합니다.
     *
     * @param review 방금 저장한 리뷰 객체 (이미지들이 이 리뷰에 연결됨)
     * @param imageUrls 사용자가 보낸 이미지 URL 리스트
     * @return ReviewImageEntity 리스트 (DB에 저장 가능)
     */
    public static List<ReviewImageEntity> toReviewImageEntities(ReviewEntity review, List<String> imageUrls) {
        return imageUrls.stream()
                .map(url -> ReviewImageEntity.builder()
                        .review(review)       // 이 이미지를 어떤 리뷰에 연결할지 설정
                        .rvImageUrl(url)      // 이미지의 실제 URL
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * DB에서 조회한 ReviewEntity를 사용자에게 전달할 ReviewResponse로 변환합니다.
     *
     * - 답글 정보는 포함하지 않습니다. (이번 구현에서는 답글 기능 제외)
     * - 작성자 닉네임, 리뷰 본문, 평점, 이미지 URL, 작성일시만 포함됩니다.
     *
     * @param review 리뷰 엔티티 (DB에서 조회된 객체)
     * @return 사용자에게 전달할 응답 DTO
     */
    public static ReviewResponse toReviewResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .nickname(review.getUser().getUserNickName())  // 작성자 닉네임
                .content(review.getRvContent())                // 리뷰 본문
                .score(review.getRvScore())                    // 평점
                .imageUrls(                                    // 이미지 URL 리스트 (null 방지)
                        review.getImageList() != null
                                ? review.getImageList().stream()
                                .map(ReviewImageEntity::getRvImageUrl)
                                .toList()
                                : List.of()
                )
                .createdAt(review.getRvCreate())               // 리뷰 작성 시간
                .build();
    }
}
