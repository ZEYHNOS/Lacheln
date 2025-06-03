package aba3.lucid.domain.payment.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayDetailRequest {
    // 쿠폰 이름
    private String couponName;

    // 해당 상품의 업체 id
    private Long cpId;

    // 상품 ID
    private Long pdId;

    // 상품 이름
    private String productName;

    // 상품 이미지
    private String imageUrl;

    // 상품 가격
    private BigInteger payCost;

    // 상품 할인 가격
    private BigInteger payDcPrice;

    // 일정 날짜
    private LocalDateTime scheduleDate;

    // 작업 시간
    private LocalTime taskTime;

    // 카테고리
    private CompanyCategory category;

    // 해당 상품 옵션 리스트
    private List<PayDetailOptionRequest> payDetailOptionEntityList;

}
