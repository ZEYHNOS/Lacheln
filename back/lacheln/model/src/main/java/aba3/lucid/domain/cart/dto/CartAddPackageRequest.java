package aba3.lucid.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class CartAddPackageRequest {

    // 패키지 정보들
    private Long packId;
    private String packName;
    private BigInteger totalPrice;
    private BigInteger discountPrice;
    private String packImageUrl;
    private LocalTime taskTime;
    private LocalDateTime startTime;

    // 패키지에 속한 상품들
    private List<CartAddProductRequest> cartAddProductRequest;

}
