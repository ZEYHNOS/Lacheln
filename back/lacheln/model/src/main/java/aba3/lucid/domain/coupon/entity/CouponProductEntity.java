package aba3.lucid.domain.coupon.entity;

import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name="coupon_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponProductEntity {

    @Id
    @Column(name = "coupon_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponProductId; //상품 쿠폰 ID

    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon; //쿠폰ID

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product; //상품ID
}
