package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CartDetailResponse {
    private Long cartDtId;
    private int quantity;
    private String optionName;
    private String optionDetailName;
    private int detailQuantity;
    private int detailTaskTime;
}
