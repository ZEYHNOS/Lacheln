package aba3.lucid.domain.coupon;

import aba3.lucid.domain.company.CompanyEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    private CouponEntity coupon;

}
