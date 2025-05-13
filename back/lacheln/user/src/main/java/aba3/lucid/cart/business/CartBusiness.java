package aba3.lucid.cart.business;

import aba3.lucid.cart.service.CartService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
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
    
    // 장바구니에 담기
    public API<String> addCart(String userId, List<CartAddRequest> cartAddRequest)    {
        UsersEntity user = userService.findByIdWithThrow(userId);
        if(cartAddRequest != null && !cartAddRequest.isEmpty()) {
            for(CartAddRequest cartRequest : cartAddRequest) {
                List<CartDetailEntity> cartDetail = new ArrayList<>();
                CartEntity cart = CartEntity
                        .builder()
                        .users(user)
                        .productId(cartRequest.getPdId())
                        .productName(cartRequest.getPdName())
                        .cartDate(cartRequest.getCartDate())
                        .cartQuantity(cartRequest.getCartQuantity())
                        .price(cartRequest.getPdPrice())
                        .taskTime(cartRequest.getPdTaskTime())
                        .build();
                for(CartDetailAddRequest cartDetailRequest : cartRequest.getPdDetails())    {
                    CartDetailEntity cartDetailEntity = CartDetailEntity
                            .builder()
                            .cart(cart)
                            .optionName(cartDetailRequest.getOpName())
                            .optionDetailName(cartDetailRequest.getOpDtName())
                            .optionDetailId(cartDetailRequest.getOpDtId())
                            .optionId(cartDetailRequest.getOpId())
                            .cartDtQuantity(cartDetailRequest.getCartDtQuantity())
                            .optionPrice(cartDetailRequest.getOpPrice())
                            .optionTaskTime(cartDetailRequest.getOpTaskTime())
                            .build();
                    cartDetail.add(cartDetailEntity);
                }
                if(!cartDetail.isEmpty()) {
                    cart.addCartDetail(cartDetail);
                }
                cartService.addCart(cart);
            }
        }
        return API.OK("장바구니에 담기 성공..");
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
