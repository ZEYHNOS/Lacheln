package aba3.lucid.domain.common.coupon;

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
public class CouponProductEntity {
    @Id
    private String couponId;

    private String productId;
}
