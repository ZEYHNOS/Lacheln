package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    /**
     * 상품 ID 기준으로 해당 상품에 작성된 리뷰 리스트를 조회하는 메서드
     * - ReviewEntity.product.productId 기준으로 자동으로 쿼리 생성됨
     *
     * @param productId 상품의 ID
     * @return ReviewEntity 리스트 (해당 상품에 대한 모든 리뷰)
     */
    List<ReviewEntity> findByProductProductId(Long productId);
}
