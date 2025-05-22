package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Builder
public class CartDetailUpdateRequest {
    private CartDetailRequest cartDetailRequest;
}
