package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
public class CartDetailAddRequest {
    private Long opId;
    private String opName;
    private int cartDtQuantity;
    private Long opDtId;
    private String opDtName;
    private BigInteger opPrice;
    private LocalTime opTaskTime;
}
