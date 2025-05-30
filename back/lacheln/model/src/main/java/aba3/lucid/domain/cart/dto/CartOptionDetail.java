package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Builder
public class CartOptionDetail {
    private Long optionId;
    private Long optionDetailId;
    private String optionName;
    private String optionDetailName;
    private LocalTime optionTaskTime;
    private BigInteger price;
    private Integer quantity;
}
