package aba3.lucid.domain.review;


import aba3.lucid.domain.product.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="review")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long reviewId;

    @Column(name = "schedule_id", nullable = false)
    private long scheduleId;

    @Column(name = "rv_content", columnDefinition = "char(255)", nullable = false)
    private String rvContent;

    @Column(name = "rv_create", nullable = false)
    private LocalDateTime rvCreate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rv_status", columnDefinition = "char(20)", nullable = false)
    private ReviewStatus reviewStatus; //등록 수정 삭제

    @Column(name = "rv_score", nullable = false)
    private double rvScore;
}
