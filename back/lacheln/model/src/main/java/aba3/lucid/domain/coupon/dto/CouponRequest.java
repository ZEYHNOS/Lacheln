package aba3.lucid.domain.coupon.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CouponRequest {

    // 쿠폰 이름
    @Length(min = 5, max = 50)
    private String name;

    // 쿠폰 내용
    @Length(max = 255)
    @NotBlank
    private String content;

    // 쿠폰 할인율
    @Min(5) @Max(90)
    private Integer discountRate;

    // 쿠폰 가격 하한선
    @Min(0)
    @NotNull
    private BigInteger minimumCost;

    // 쿠폰 가격 상한선
    @Max(100_000_000)
    @NotNull
    private BigInteger maximumCost;

    // 쿠폰 발급일
    @Past
    private LocalDateTime createDate;

    // 쿠폰 만료일
    @Future
    // TODO 발급일보다 만료일이 더 후인지 확인하는 검증 로직 필요
    private LocalDateTime expirationDate;

}
