package aba3.lucid.domain.review.entity;


import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.payment.entity.PayManagementEntity;
import aba3.lucid.domain.product.enums.ReviewCommentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name="review_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentEntity {
    // 답글 ID
    @Id
    @Column(name = "rv_comment_id", nullable = false)
    private long rvCommentId;

    // 리뷰
    @OneToOne
    @JoinColumn(name = "reivew_id")
    private ReviewEntity reivew;

    // 업체
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    // 결제관리
    @OneToOne
    @JoinColumn(name = "pay_id")
    private PayManagementEntity payManagement;

    //리뷰이미지
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImageEntity> imageList = new ArrayList<>();

    // 내용
    @Column(name = "rvc_content", columnDefinition = "char(255)", nullable = false)
    private String rvcContent;  //답글 content

    // 작성일
    @Column(name = "rvc_create", nullable = false)
    private LocalDate rvcCreate;

    // 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "rvc_status", columnDefinition = "char(20)", nullable = false)
    private ReviewCommentStatus rvcStatus; // 답글  표시 숨기기 삭제
}
