package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "product_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageEntity {

    @Id
    @Column(name = "pd_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdImageId;

    @ToString.Exclude
    @JoinColumn(name = "pd_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @Column(name = "pd_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String pdImageUrl;

}
