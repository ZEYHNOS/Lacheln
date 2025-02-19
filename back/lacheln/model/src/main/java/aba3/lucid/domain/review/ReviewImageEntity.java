package aba3.lucid.domain.review;


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
    private long imageId;

    @Column(name = "review_id", nullable = false)
    private long reviewId;  //주후 테이블 관계 설정

    @Column(name = "rv_image_url", length = 255, nullable = false)
    private String rvImageUrl;
}
