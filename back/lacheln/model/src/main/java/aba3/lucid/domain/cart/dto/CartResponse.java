package aba3.lucid.domain.cart.dto;

import aba3.lucid.domain.cart.entity.CartEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private Long cartId;
    private Long productIds;
    private List<Long> productDetailIds;
}
