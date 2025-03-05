package aba3.lucid.domain.review;

import aba3.lucid.domain.payment.PayManagementEntity;
import aba3.lucid.domain.product.ProductEntity;
import aba3.lucid.domain.user.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class ReviewEntity {

    // 리뷰 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    // 결제관리
    @OneToOne
    @JoinColumn(name = "pay_id")
    private PayManagementEntity payManagement;

    // 상품
    @OneToOne
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // 소비자
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    // 리뷰 내용
    @Column(name = "rv_content")
    private String rvContent;

    // 리뷰 작성시간
    @Column(name = "rv_create")
    private LocalDateTime rvCreate;

    // 리뷰 상태
    @Column(name = "rv_status")
    private String rvStatus;

    // 리뷰 평점
    @Column(name = "rv_score")
    private double rvScore;
}
