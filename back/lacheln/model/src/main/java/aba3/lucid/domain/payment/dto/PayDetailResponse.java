package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailResponse {
    private Long payDetailId;
    private Long cpId;
    private Long couponId;
    private String productName;
    private BigInteger payCost;
    private BigInteger payDcPrice;
    private List<PayDetailOptionResponse> options;
}