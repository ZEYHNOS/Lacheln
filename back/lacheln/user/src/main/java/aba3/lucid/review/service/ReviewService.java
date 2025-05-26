package aba3.lucid.review.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.payment.repository.PayManagementRepository;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.review.entity.ReviewImageEntity;
import aba3.lucid.domain.review.repository.ReviewImageRepository;
import aba3.lucid.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final PayManagementRepository payManagementRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    /**
     * 리뷰 등록 처리 (트랜잭션 단위)
     */
    @Transactional
    public void createReview(ReviewEntity review, List<ReviewImageEntity> imageList) {
        reviewRepository.save(review);

        if (imageList != null && !imageList.isEmpty()) {
            reviewImageRepository.saveAll(imageList);
        }
    }

    /**
     * 리뷰 삭제 처리 (Soft Delete)
     */
    @Transactional
    public void deleteReview(ReviewEntity review) {
        // markAsDeleted()는 비즈니스 계층에서 호출된 상태
        // 여기서는 실제로 DB에 반영만 진행
        // dirty checking으로 반영됨 (JPA flush 시점에 반영)
    }

    /**
     * 결제 관리 엔티티 조회 및 사용자 검증
     */
    @Transactional(readOnly = true)
    public PayManagementEntity getPayManagementIfValid(String userId, String payId) {
        PayManagementEntity pay = payManagementRepository.findById(payId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 결제 정보를 찾을 수 없습니다."));

        if (!pay.getUser().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "본인이 결제한 상품에만 리뷰 작성이 가능합니다.");
        }

        return pay;
    }

    /**
     * 결제 상세 정보 중 첫 번째 항목 반환
     */
    @Transactional(readOnly = true)
    public PayDetailEntity getFirstPayDetailOrThrow(PayManagementEntity pay) {
        List<PayDetailEntity> list = pay.getPayDetailEntityList();

        if (list == null || list.isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "결제 상세 정보가 존재하지 않습니다.");
        }

        return list.get(0); // 첫 항목 반환
    }

    /**
     * 결제 상세 정보에서 상품 정보를 추출
     * - 현재는 연관관계가 없기 때문에 pdId만 전달되어 있어 실제 ProductEntity는 null 처리됨
     * - 추후 ProductEntity와 연관관계를 설정하거나 Service 단에서 조회 필요
     */
    public ProductEntity getProductFromPayDetail(PayDetailEntity payDetail) {
        // 현재 구조에서는 ProductEntity와 직접 매핑이 없으므로 null 반환
        // 필요 시 productRepository.findById(payDetail.getPdId()) 등으로 조회 추가 가능
        return null;
    }
}