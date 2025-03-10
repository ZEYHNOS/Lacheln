package aba3.lucid.domain.coupon;

import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.user.UsersEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "coupon_box")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponBoxEntity {

    @Id
    @Column(name = "coupon_box_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponBoxId;//쿠폰함ID

    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsersEntity users;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status", columnDefinition = "CHAR(20)", nullable = false)
    private CouponBoxStatus couponStatus; //사용, 미사용, 만료
}
