package aba3.lucid.Coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Getter
@Setter
@Table(name="product_coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCoupon {
    @Id
    private String couponId;

    private String productId;
}
