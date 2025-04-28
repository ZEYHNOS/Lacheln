package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CartAddRequest {
    private Long pdId;
    private LocalDateTime cartDate;
    private int cartQuantity;
    private String pdName;
    private BigInteger pdPrice;
    private int pdTaskTime;
    private List<CartDetailAddRequest> pdDetails;
}