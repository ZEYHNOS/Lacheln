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
    private long piId;

    // 외래키(상품ID)
    // TODO ManyToOne 설정
    @Column(nullable = false)
    private long pdId;

    // 컬럼명(NULL)
    @Column(nullable = true)
    private String piTitle;

    // 컬럼 내용(NULL)
    @Column(nullable = true)
    private String piContent;

    // 이미지 URL(NULL)
    @Column(nullable = true)
    private String piImageUrl;
}
