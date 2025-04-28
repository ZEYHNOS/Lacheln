package aba3.lucid.domain.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartAddResponse {
    private Long productIds;
    private List<Long> productDetailIds;
}
