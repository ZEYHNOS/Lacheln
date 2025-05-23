package aba3.lucid.domain.review.entity;

import aba3.lucid.domain.payment.entity.PayDetailEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private long reviewId;

    // 결제 관리 정보 (1:1)
    @OneToOne
    @JoinColumn(name = "pay_id")
    private PayManagementEntity payManagement;

    // 결제 상세 정보 (1:1)
    @OneToOne
    private PayDetailEntity payDetailEntity;

    // 구매한 상품 (1:1)
    @OneToOne
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // 리뷰 작성자 (1:1)
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    // 리뷰 본문
    @Column(name = "rv_content", nullable = false)
    private String rvContent;

    // 리뷰 작성일
    @Column(name = "rv_create", nullable = false)
    private LocalDateTime rvCreate;

    // 리뷰 상태 (예: REGISTERED, DELETED 등)
    @Column(name = "rv_status", nullable = false)
    private String rvStatus;

    // 평점 (0.0 ~ 5.0)
    @Column(name = "rv_score", nullable = false)
    private double rvScore;

    // 리뷰 이미지 리스트 (1:N)
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImageEntity> imageList;

    // 리뷰 논리 삭제 시각 (nullable)
    @Column(name = "rv_deleted_at")
    private LocalDateTime rvDeletedAt;

    /**
     * 리뷰 논리 삭제 처리
     * - 상태를 'DELETED'로 변경하고 삭제 시각 기록
     */
    public void markAsDeleted() {
        this.rvStatus = "DELETED";
        this.rvDeletedAt = LocalDateTime.now();
    }
}
