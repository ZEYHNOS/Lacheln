package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long pdImageId;

    // ManyToOne
    private long pdId;

    @Column(name = "pd_image_url", columnDefinition = "CHAR(255)", nullable = false)
    private String pdImageUrl;

}
