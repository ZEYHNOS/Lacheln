package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long> {
}
