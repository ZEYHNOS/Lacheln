package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.entity.ReviewEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long> {

    void deleteByReviewId(Long reviewId);

    Optional<ReviewCommentEntity> findByReviewId(Long reviewId);

    List<ReviewCommentEntity> findAllByReviewIdIn(List<Long> reviewIdList);

}
