package aba3.lucid.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartPaymentRequest {

    private Long cartId;

    private LocalDateTime startDate;

    private LocalTime taskTime;

}
