package aba3.lucid.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "product_info")
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoEntity {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pdInfoId;

    // 외래키(상품)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // TODO 외래키(업체언어) 제작하기

    @Column(name = "pd_info_content", columnDefinition = "VARCHAR(255)", nullable = false)
    private String content;
}
