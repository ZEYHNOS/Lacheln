package aba3.lucid.domain.coupon.dto;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CouponResponse {

    private String couponId; // 쿠폰 Id

    private String companyName; // 업체 이름

    private String productName; // 상품 이름(Null 허용)

    private String couponName; //쿠폰이름

    private String content; //내용

    private BigInteger discountRate; //할인율 5~90%

    private BigInteger minimumCost; //가격하한선 0

    private BigInteger maximumCost; //가격상한선 1억

    private LocalDateTime createDate; //발급일 now

    private LocalDateTime expirationDate; //만료일 2099-12-31

}
