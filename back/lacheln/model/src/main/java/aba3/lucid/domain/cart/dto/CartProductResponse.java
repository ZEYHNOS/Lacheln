package aba3.lucid.domain.cart.dto;

import aba3.lucid.domain.company.enums.CompanyCategory;
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
    private String manager;
    private CompanyCategory category;
    private List<CartDetailResponse> cartDetails;

}
