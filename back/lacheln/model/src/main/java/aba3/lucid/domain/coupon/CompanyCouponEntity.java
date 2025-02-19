package aba3.lucid.domain.coupon;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "coupon_cp")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyCouponEntity {

    @Id
    @Column(name = "coupon_cp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponCpId;

    @Column(name = "cp_id")
    private long cpId; //업체ID

    @Column(name = "coupon_id", columnDefinition = "CHAR(15)", nullable = false)
    private String couponId; //쿠폰ID
}
