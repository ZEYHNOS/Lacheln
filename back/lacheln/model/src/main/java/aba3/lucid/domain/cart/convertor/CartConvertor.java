package aba3.lucid.domain.cart.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartDetailResponse;
import aba3.lucid.domain.cart.dto.CartResponse;
import aba3.lucid.domain.cart.entity.CartDetailEntity;
import aba3.lucid.domain.cart.entity.CartEntity;

import java.util.List;

@Converter
public class CartConvertor {
    public List<CartResponse> convertToResponses(List<CartEntity> cartEntityList)  {
        return cartEntityList
                .stream()
                .map(cart -> CartResponse.builder()
                        .cartId(cart.getCartId())
                        .cartQuantity(cart.getCartQuantity())
                        .cartDetails(convertToResponseDetails(cart.getCartDetails()))
                        .productId(cart.getProductId())
                        .cpId(cart.getCpId())
                        .price(cart.getPrice())
                        .productName(cart.getProductName())
                        .pdImageUrl(cart.getPdImageUrl())
                        .startTime(cart.getCartDate())
                        .taskTime(cart.getTaskTime())
                        .build())
                .toList();
    }

    public List<CartDetailResponse> convertToResponseDetails(List<CartDetailEntity> cartDetailEntityList)  {
        return cartDetailEntityList
                .stream()
                .map(details -> CartDetailResponse.builder()
                        .cartDtId(details.getCartDtId())
                        .quantity(details.getCartDtQuantity())
                        .detailPrice(details.getOptionPrice())
                        .detailTaskTime(details.getOptionTaskTime())
                        .optionName(details.getOptionName())
                        .optionDetailName(details.getOptionDetailName())
                        .build())
                .toList();
    }
}
