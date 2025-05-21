package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailBlockOptionResponse {
    private long payDetailOptionId;
    private String payOpName;
    private String payOpDtName;
    private int payDtQuantity;
    private BigInteger payOpPlusCost;
    private Integer payOpTaskTime;
}
