package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Builder
public class CartDetailRequest {
    private Long opId;
    private Integer cartDtQuantity;
    private Long opDtId;
    private String opName;
    private String opDtName;
    private BigInteger opPrice;
    private LocalTime opTaskTime;
}
