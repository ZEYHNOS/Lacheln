package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long> {

    /**
     * 특정 리뷰에 대해 해당 업체(판매자)가 이미 답글을 작성했는지 여부 확인
     *
     * @param review  대상 리뷰
     * @param company 답글 작성자 (판매자)
     * @return true: 이미 작성함, false: 작성 가능
     */
    boolean existsByReviewAndCompany(ReviewEntity review, CompanyEntity company);
}
