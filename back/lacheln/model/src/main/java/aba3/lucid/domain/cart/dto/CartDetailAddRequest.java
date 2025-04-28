package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class CartDetailAddRequest {
    private Long opId;
    private String opName;
    private int cartDtQuantity;
    private Long opDtId;
    private String opDtName;
    private BigInteger opPrice;
    private int opTaskTime;
}
