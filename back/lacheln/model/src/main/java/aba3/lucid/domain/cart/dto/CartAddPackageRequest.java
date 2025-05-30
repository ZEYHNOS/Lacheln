package aba3.lucid.domain.cart.dto;

import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class CartAddPackageRequest {

    private Long packId;
    private String packName;
    private BigInteger discountPrice;
    private String packImageUrl;
    private LocalTime taskTime;
    private LocalDateTime startTime;

    private List<CartAddProductRequest> cartAddProductRequest;

}
