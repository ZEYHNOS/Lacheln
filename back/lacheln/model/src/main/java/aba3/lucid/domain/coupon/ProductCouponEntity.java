package aba3.lucid.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name="product_coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCouponEntity {

    @Id
    @Column(name = "coupon_id", columnDefinition = "CHAR(15)")
    private String couponId;

    @Id
    @Column(name = "pd_id")
    private long pdId;
}
