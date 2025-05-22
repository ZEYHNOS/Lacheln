package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CartAddRequest {
    private Long pdId;
    private LocalDateTime cartDate;
    private int cartQuantity;
    private String pdName;
    private BigInteger pdPrice;
    private LocalTime pdTaskTime;
    private String pdImageUrl;
    private List<CartDetailAddRequest> pdDetails;
}