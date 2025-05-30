package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class CartProductResponse {

    private Long cartId;
    private Long pdId;
    private String pdName;
    private String pdImageUrl;
    private BigInteger price;
    private String cpName;
    private Long cpId;
    private LocalDateTime startTime;
    private LocalTime taskTime;
    private Integer cartQuantity;
    private List<CartDetailResponse> cartDetails;

}
