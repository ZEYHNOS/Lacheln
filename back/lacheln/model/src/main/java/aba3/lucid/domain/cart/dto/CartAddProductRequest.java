package aba3.lucid.domain.cart.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartAddProductRequest {
    // 상품에 대한 정보들
    private Long cpId;
    private Long pdId;
    private String cpName;
    private String pdName;
    private String pdImageUrl;
    private BigInteger pdPrice;
    private Integer cartQuantity;
    private LocalTime taskTime;
    private LocalDateTime startDatetime;
    private String manager;
    private CompanyCategory category;

    // 옵션 리스트
    private List<CartOptionDetail> optionDetails;
}