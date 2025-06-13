package aba3.lucid.domain.review.repository;

import aba3.lucid.domain.review.entity.ReviewImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Long> {
    List<ReviewImageEntity> findAllByReview_ReviewId(Long id);
    void deleteAllByReview_ReviewId(Long id);
}
