package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailBlockResponse {
    private Long payDetailId;
    private Long cpId;
    private Long pdId;
    private Long couponId;
    private String productName;
    private BigInteger payCost;
    private BigInteger payDcPrice;
    private LocalDateTime startTime;
    private LocalTime taskTime;
    private List<PayDetailBlockOptionResponse> options;
}