package aba3.lucid.domain.coupon;

import aba3.lucid.domain.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name="product_coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCouponEntity {

    @Id
    @Column(name = "product_coupon_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;
}
