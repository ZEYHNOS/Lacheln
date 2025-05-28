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
    private Long pdId;
    private LocalDateTime startTime;
    private LocalTime taskTime;
    private List<PayDetailBlockOptionResponse> options;
}