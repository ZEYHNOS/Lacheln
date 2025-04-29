package aba3.lucid.domain.coupon.entity;

import aba3.lucid.common.validate.Validator;
import aba3.lucid.domain.coupon.enums.CouponBoxStatus;
import aba3.lucid.domain.user.entity.UsersEntity;
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
    private Long couponBoxId;//쿠폰함ID

    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status", columnDefinition = "CHAR(20)", nullable = false)
    private CouponBoxStatus couponStatus; //사용, 미사용, 만료

    public void updateStatus(CouponBoxStatus couponBoxStatus) {
        Validator.throwIfNull(couponBoxStatus);

        this.couponStatus = couponBoxStatus;
    }
}
