package aba3.lucid.review.service;


import aba3.lucid.domain.review.entity.ReviewCommentEntity;
import aba3.lucid.domain.review.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;

    @Transactional
    public ReviewCommentEntity addComment(ReviewCommentEntity entity) {
        ReviewCommentEntity savedEntity = reviewCommentRepository.save(entity);
        return savedEntity;
    }

    public void deleteComment(ReviewCommentEntity reviewComment) {
        reviewCommentRepository.delete(reviewComment);
    }

}
