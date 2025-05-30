package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CartAddProductRequest {
    private Long cpId;
    private Long pdId;
    private String cpName;
    private String pdName;
    private String pdImageUrl;
    private BigInteger pdPrice;
    private Integer pdQuantity;
    private LocalDateTime startDateTime;
    private LocalTime taskTime;
    
    // 옵션 리스트
    private List<CartOptionDetail> optionDetails;
}