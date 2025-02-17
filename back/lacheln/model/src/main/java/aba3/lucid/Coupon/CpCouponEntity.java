package aba3.lucid.Coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "cp_coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CpCouponEntity {
    @Id
    private String couponId;

    private String companyId;


}
