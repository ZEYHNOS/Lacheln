package aba3.lucid.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "cp_coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCopanyEntity {
    @Id
    private String couponId;

    private String companyId;


}
