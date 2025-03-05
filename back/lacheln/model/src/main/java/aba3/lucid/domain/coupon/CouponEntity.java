package aba3.lucid.domain.coupon;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="coupon")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponEntity {

    @Id
    @Column(name = "coupon_id", columnDefinition = "CHAR(15)")
    private String couponId; //'-' 하이폰은 저장하지 않음

    @Column(name = "coupon_name", length = 50, nullable = false)
    private String couponName; //쿠폰이름

    @Column(name = "coupon_content", length = 255)
    private String couponContent; //내용

    @Column(name = "coupon_discount_rate", nullable = false)
    private BigInteger couponDiscountRate; //할인율 5~90%

    @Column(name = "coupon_minimumcost", nullable = false)
    private BigInteger couponMinimumCost; //가격하한선 0

    @Column(name = "coupon_maximumcost", nullable = false)
    private BigInteger couponMaximumCost; //가격상한선 1억

    @Column(name = "coupon_create_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime couponCreateDate; //발급일 now

    @Column(name = "coupon_expiration_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT '2099-12-31 23:59:59'")
    private LocalDateTime couponExpirationDate; //만료일 2099-12-31
}