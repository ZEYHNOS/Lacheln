package aba3.lucid.domain.coupon;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name="coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponEntity {
    @Id
    private String couponId;

    private String couponName;

    private String content;//내용

    private String discountRate;

    private String minimumCost; //가격 하한선

    private String maximumCost;//가격 상한선

    private String createDate;//발급일

    private String expirationDate;//만료일
}
