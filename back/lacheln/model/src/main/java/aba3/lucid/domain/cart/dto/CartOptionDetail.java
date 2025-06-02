package aba3.lucid.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartOptionDetail {
    private Long pdId;
    private Long opId;
    private Long opDtId;
    private String opName;
    private String opDtName;
    private LocalTime opTasktime;
    private BigInteger opPrice;
    private Integer cartDtQuantity;
}
