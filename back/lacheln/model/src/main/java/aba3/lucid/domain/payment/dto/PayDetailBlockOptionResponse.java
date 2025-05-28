package aba3.lucid.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class PayDetailBlockOptionResponse {
    private LocalTime payOpTaskTime;
}
