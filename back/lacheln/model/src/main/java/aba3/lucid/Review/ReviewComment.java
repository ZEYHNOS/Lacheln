package aba3.lucid.Review;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Getter
@Table(name="review_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewComment {
    @Id
    @Column(name = "review_id", nullable = false)
    private long reviewId;

    @Column(name = "cp_id", nullable = false)
    private long cpId;

    @Column(name = "rvc_content", columnDefinition = "char(255)", nullable = false)
    private String rvcContent;  //답글 content

    @Column(name = "rvc_create", nullable = false)
    private LocalDate rvcCreate;

    @Enumerated(EnumType.STRING)
    @Column(name = "rvc_status", columnDefinition = "char(20)", nullable = false)
    private RvcStatus rvcStatus; // 답글  표시 숨기기 삭제




}
