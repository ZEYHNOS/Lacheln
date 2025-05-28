package aba3.lucid.domain.cart.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.cart.dto.CartDetailRequest;
import aba3.lucid.domain.cart.dto.CartDetailResponse;
import aba3.lucid.domain.cart.dto.CartRequest;
import aba3.lucid.domain.cart.dto.CartResponse;
import aba3.lucid.domain.cart.entity.CartDetailEntity;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.user.entity.UsersEntity;

import java.util.ArrayList;
import java.util.List;

@Converter
public class CartConvertor {

    public CartEntity convertToEntity(UsersEntity user, CartRequest request)  {
        return CartEntity.builder()
                .users(user)
                .pdImageUrl(request.getPdImageUrl())
                .cartQuantity(request.getCartQuantity())
                .price(request.getPdPrice())
                .startDatetime(request.getStartDateTime())
                .cpId(request.getPdId())
                .pdId(request.getPdId())
                .taskTime(request.getPdTaskTime())
                .pdName(request.getPdName())
                .build();
    }

    public CartDetailEntity convertToEntity(CartEntity cart, CartDetailRequest request)   {
        return CartDetailEntity.builder()
                .cart(cart)
                .optionName(request.getOpName())
                .cartDtId(request.getOpDtId())
                .cartDtQuantity(request.getCartDtQuantity())
                .optionDetailName(request.getOpDtName())
                .optionPrice(request.getOpPrice())
                .optionId(request.getOpId())
                .optionTaskTime(request.getOpTaskTime())
                .build();
    }

    public CartResponse convertToDto(CartEntity cart, List<CartDetailEntity> request)   {
        List<CartDetailResponse> response = convertToDto(request);
        return CartResponse.builder()
                .startTime(cart.getStartDatetime())
                .cartQuantity(cart.getCartQuantity())
                .price(cart.getPrice())
                .cpId(cart.getCpId())
                .pdImageUrl(cart.getPdImageUrl())
                .pdName(cart.getPdName())
                .cartId(cart.getCartId())
                .taskTime(cart.getTaskTime())
                .pdId(cart.getPdId())
                .cartDetails(response)
                .build();
    }

    public List<CartDetailResponse> convertToDto(List<CartDetailEntity> entities)   {
        return entities.stream()
                .map(details -> CartDetailResponse.builder()
                        .optionDetailName(details.getOptionDetailName())
                        .optionName(details.getOptionName())
                        .quantity(details.getCartDtQuantity())
                        .detailPrice(details.getOptionPrice())
                        .detailTaskTime(details.getOptionTaskTime())
                        .cartDtId(details.getCartDtId())
                        .build())
                .toList();
    }

    public List<CartResponse> convertToResponses(List<CartEntity> cartEntityList)  {
        return cartEntityList
                .stream()
                .map(cart -> CartResponse.builder()
                        .cartId(cart.getCartId())
                        .cartQuantity(cart.getCartQuantity())
                        .cartDetails(convertToResponseDetails(cart.getCartDetails()))
                        .pdId(cart.getPdId())
                        .cpId(cart.getCpId())
                        .price(cart.getPrice())
                        .pdName(cart.getPdName())
                        .pdImageUrl(cart.getPdImageUrl())
                        .startTime(cart.getStartDatetime())
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
