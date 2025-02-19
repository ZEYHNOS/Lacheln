package aba3.lucid.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "coupon_box")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponBoxEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String userId; //FK

    @Column(name = "coupon_id", columnDefinition = "CHAR(15)", nullable = false)
    private String couponId; //쿠폰ID

    @Column(name = "coupon_status", columnDefinition = "CHAR(20)", nullable = false)
    private String couponStatus; //사용, 미사용, 만료
}
