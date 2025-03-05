package aba3.lucid.domain.coupon;

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

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String user; //소비자ID

    @Column(name = "coupon_id", columnDefinition = "CHAR(15)", nullable = false)
    private String coupon; //쿠폰ID

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status", columnDefinition = "CHAR(20)", nullable = false)
    private String couponStatus; //사용, 미사용, 만료
}
