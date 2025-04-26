package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CartDeleteRequest {
    private List<Long> cartIds;
}