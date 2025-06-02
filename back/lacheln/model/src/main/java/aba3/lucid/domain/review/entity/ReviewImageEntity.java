package aba3.lucid.domain.review.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="review_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @Column(name = "rv_image_url", columnDefinition = "VARCHAR(255)", nullable = false)
    private String rvImageUrl;
}
