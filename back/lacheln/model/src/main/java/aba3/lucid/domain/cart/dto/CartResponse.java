package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class CartResponse {
    private Long cpId;
    private Long cartId;
    private Long productId;
    private int cartQuantity;
    private String productName;
    private BigInteger price;
    private String pdImageUrl;
    private LocalDateTime startTime;
    private LocalTime taskTime;
    private List<CartDetailResponse> cartDetails;
}
