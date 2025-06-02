package aba3.lucid.domain.cart.dto;

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
    private Long cpId;
    private Long pdId;
    private String cpName;
    private String pdName;
    private String pdImageUrl;
    private BigInteger pdPrice;
    private Integer cartQuantity;
    private LocalDateTime startDateTime;
    private LocalTime taskTime;
    
    // 옵션 리스트
    private List<CartOptionDetail> optionDetails;
}