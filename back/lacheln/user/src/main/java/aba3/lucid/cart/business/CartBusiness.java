package aba3.lucid.cart.business;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.convertor.CartConvertor;
import aba3.lucid.domain.cart.dto.*;
import aba3.lucid.domain.cart.entity.CartDetailEntity;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CartBusiness {

    private final CartService cartService;
    private final UserService userService;
    private final CartConvertor cartConvertor;

    // 장바구니 리스트 검색
    public API<List<CartResponse>> getCartList(String userId)    {
        List<CartEntity> cart = cartService.getCartByUserIdWithThrow(userId);
        return API.OK(cartConvertor.convertToResponses(cart));
    }

    // 장바구니 업데이트
    public API<CartUpdateResponse> cartUpdate(String userId, CartUpdateRequest request)    {
        UsersEntity user = userService.findByIdWithThrow(userId);
        CartEntity cart = null;
        List<CartDetailEntity> cartDetails = new ArrayList<>();

        if(user != null && request != null) {
            cart = cartConvertor.convertToEntity(user, request.getCartRequest());
            for(CartDetailRequest details : request.getCartRequest().getPdDetails())   {
                cartDetails.add(cartConvertor.convertToEntity(cart, details));
            }
            cart = cartService.addCart(cart);
        } else {
            throw new ApiException(ErrorCode.GONE, "해당하는 유저가 없거나, 요청된 값이 없습니다.");
        }

        CartUpdateResponse response = CartUpdateResponse.builder()
                .updatedCart(cartConvertor.convertToDto(cart, cartDetails))
                .build();

        return API.OK(response, "업데이트 완료");
    }
    
    // 장바구니에 담기
    public API<CartAddResponse> addCart(String userId, CartAddRequest cartAddRequest)    {
        UsersEntity user = userService.findByIdWithThrow(userId);
        List<CartDetailEntity> cartDetail = new ArrayList<>();
        CartEntity cart = null;

        if(cartAddRequest != null && user != null) {
            cart = cartConvertor.convertToEntity(user, cartAddRequest.getCartRequests());
            for(CartDetailRequest cartDetailRequest : cartAddRequest.getCartRequests().getPdDetails())    {
                cartDetail.add(cartConvertor.convertToEntity(cart, cartDetailRequest));
            }
            if(!cartDetail.isEmpty()) {
                cart.addCartDetail(cartDetail);
            }
            cart = cartService.addCart(cart);
        } else {
            throw new ApiException(ErrorCode.GONE,"해당하는 유저가 없거나, 요청된 값이 없습니다.");
        }

        CartAddResponse response = CartAddResponse.builder()
                .addedCart(cartConvertor.convertToDto(cart, cartDetail))
                .build();

        return API.OK(response, "장바구니에 담기 성공..");
    }
    
    // 장바구니 상품제거
    public API<String> deleteCart(CartDeleteRequest cartDeleteRequest)    {
        cartService.removeCart(cartDeleteRequest.getCartIds());
        return API.OK("장바구니 상품 제거 성공..");
    }

    // 장바구니 비우기
    public API<String> deleteAllCart(String userId)  {
        cartService.removeAllCart(userId);
        return API.OK("장바구니 비우기 성공..");
    }
}
