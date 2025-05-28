package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CartUpdateRequest {
    private Long cartId;
    private CartRequest cartRequest;
}