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
public class CartAllResponse {

    private Long packId;
    private String packName;
    private String packImageUrl;
    private Long pdId;
    private String pdName;
    private String pdImageUrl;
    private Long cartId;
    private Long cpId;
    private String cpName;
    private BigInteger price;
    private BigInteger discountPrice;
    private LocalDateTime startTime;
    private LocalTime taskTime;
    private Integer cartQuantity;
    private String manager;
    private CompanyCategory category;
    private List<CartDetailResponse> cartDetails;

}
