package aba3.lucid.repository.review;

import aba3.lucid.domain.product.ReviewCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long> {
}
