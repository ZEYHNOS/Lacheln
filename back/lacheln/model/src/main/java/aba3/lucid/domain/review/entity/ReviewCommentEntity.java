package aba3.lucid.domain.review.entity;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.enums.CommentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.dto.ReviewCommentRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Table(name="review_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentEntity {
    // 답글 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvCommentId;

    // 리뷰
    @Column(nullable = false, unique = true)
    private Long reviewId;

    // 업체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;
    
    // 내용
    @Column(name = "rvc_content", columnDefinition = "char(255)")
    private String rvcContent;  //답글 content

    // 작성일
    @Column(name = "rvc_create")
    private LocalDateTime rvcCreate;

    // 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "rvc_status", columnDefinition = "char(20)", nullable = false)
    private ReviewStatus rvcStatus; // 답글  표시 숨기기 삭제

    /**
     * 답글을 논리적으로 삭제 처리하는 메서드
     *
     * - 실제 DB에서 데이터를 제거하지 않고,
     *   상태값만 'DELETED'로 변경하여 삭제된 것으로 간주
     * - 추후 1개월 후 배치 또는 스케줄러를 통해 실제 삭제 가능
     */
    public void markAsDeleted() {
        this.rvcStatus = ReviewStatus.DELETED;
    }

    public void writeReviewComment(ReviewCommentRequest request) {
        this.rvcContent = request.getContent();
        this.rvcCreate = LocalDateTime.now();
        this.rvcStatus = ReviewStatus.REGISTERED;
    }

    public void deleteRequest() {
        if (this.getRvcStatus().equals(ReviewStatus.DELETED)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        this.rvcStatus = ReviewStatus.DELETED;
    }
}
