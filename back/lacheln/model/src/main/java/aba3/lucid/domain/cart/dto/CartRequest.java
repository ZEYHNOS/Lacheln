package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CartRequest {
    private String pdName;
    private Long pdId;
    private BigInteger pdPrice;
    private String pdImageUrl;
    private List<CartOptionDetail> optionDetails;
}
