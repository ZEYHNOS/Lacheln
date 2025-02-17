package aba3.lucid.Coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "coupon_box")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponBox {
    @Id
    private String userId;

    private String couponId;

    private String couponStatus; //사용, 미사용, 만료

    private String couponDiscountCost;  //할인된 금액
}
