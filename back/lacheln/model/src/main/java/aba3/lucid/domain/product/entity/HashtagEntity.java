package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "hashtag")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HashtagEntity {

    // 상품 태그
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    // 상품 ID(외래키) 상품 Entity 조회할 때 무조건 존재해야하는 테이블
    @ToString.Exclude
    @JoinColumn(name = "pd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    // 태그 이름
    @Column(name = "tag_name", nullable = false, columnDefinition = "VARCHAR(20)")
    private String tagName;
}
