package aba3.lucid.domain.review.entity;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.product.enums.CommentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import aba3.lucid.domain.review.dto.ReviewCreateRequest;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class ReviewEntity {

    // 리뷰 ID (PK)
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // 결제 상세 정보 (1:1)
    @OneToOne
    private PayDetailEntity payDetailEntity;

    // 리뷰 작성자 (1:1)
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    // 상품 ID
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long companyId;

    // 리뷰 본문
    @Column(name = "rv_content")
    private String rvContent;

    // 리뷰 작성일
    @Column(name = "rv_create")
    private LocalDateTime rvCreate;

    // 리뷰 상태 (예: REGISTERED, DELETED 등)
    @Enumerated(EnumType.STRING)
    @Column(name = "rv_status", nullable = false)
    private ReviewStatus rvStatus;

    // 평점 (0.0 ~ 5.0)
    @Column(name = "rv_score")
    private double rvScore;

    // 리뷰 이미지 리스트 (1:N)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImageEntity> imageList;

    // 리뷰 논리 삭제 시각 (nullable)
    @Column(name = "rv_deleted_at")
    private LocalDateTime rvDeletedAt;

    public void markAsDeleted() {
        this.rvStatus = ReviewStatus.DELETED;
        this.rvDeletedAt = LocalDateTime.now();
    }

    public void updateField(ReviewCreateRequest request) {
        this.rvStatus = ReviewStatus.REGISTERED;
        this.rvCreate = LocalDateTime.now();
        this.rvContent = request.getRvContent();
        this.rvScore = request.getRvScore();
    }

    public void updateImageList(List<ReviewImageEntity> reviewImageEntityList) {
        if (this.imageList == null) {
            this.imageList = new ArrayList<>();
        }

        this.imageList.clear();

        imageList.addAll(reviewImageEntityList);
    }
}
