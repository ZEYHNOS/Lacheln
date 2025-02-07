package aba3.lucid.productimage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "productImage")
public class ProductImageEntity {
    // 상품 이미지 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdImageId;

    //TODO 상품 ID(외래키) 설정 필요
    @Column(nullable = false)
    private Long pdId;

    // 이미지 URL
    @Column(nullable = false)
    private String imageUrl;
}
