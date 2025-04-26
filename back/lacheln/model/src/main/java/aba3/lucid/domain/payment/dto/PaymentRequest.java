package aba3.lucid.domain.payment.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRequest {

    private String productName;

    private String productPrice;


}
