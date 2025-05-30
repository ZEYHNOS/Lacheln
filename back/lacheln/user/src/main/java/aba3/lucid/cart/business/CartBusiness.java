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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CartBusiness {

    private final CartService cartService;
    private final UserService userService;
    private final CartConvertor cartConverter;

    // 장바구니 리스트 검색
    public API<List<CartAllResponse>> getCartList(String userId)    {
        List<CartEntity> cart = cartService.getCartByUserId(userId);
        return API.OK(cartConverter.convertToCartAllResponses(cart));
    }
    
    // 장바구니에 개별 상품 담기
    public API<CartProductResponse> addProduct(String userId, CartAddProductRequest request)    {
        UsersEntity user = userService.findByIdWithThrow(userId);

        // request가 있는지 확인
        if(request == null) {
            throw new ApiException(ErrorCode.GONE, "요청된 값이 없습니다.");
        }

        // 현재 시간 이전의 예약을 하는지 검증
        if(request.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "과거 시간대의 예약을 진행 할 순 없습니다.");
        }

        // 카트 생성
        CartEntity cart = cartConverter.convertToEntityByPd(user, request);
        List<CartDetailEntity> cartDetail = new ArrayList<>();

        // 카트옵션에 옵션넣기
        for (CartOptionDetail detail : request.getOptionDetails()) {
            cartDetail.add(cartConverter.convertToEntityByPd(cart, detail));
        }
        
        // 생성된 카트에 생성된 옵션 넣기
        cart.addCartDetail(cartDetail);

        // 저장한 카트 불러오기
        CartEntity addedCart = cartService.addProduct(cart);
        CartProductResponse response = cartConverter.convertToDtoByPd(addedCart, cartDetail);

        // 반환
        return API.OK(response, "장바구니에 상품 담기 성공..");
    }

    // 장바구니에 패키지 담기
    public API<CartPackageResponse> addPackage(String userId, CartAddPackageRequest request)    {
        // 유저 정보 불러오기
        UsersEntity user = userService.findByIdWithThrow(userId);
        List<CartAddProductRequest> products = request.getCartAddProductRequest();


        // Request가 Null인지 확인
        if(request == null) {
            throw new ApiException(ErrorCode.GONE, "요청한 데이터가 없습니다.");
        }

        // 현재 이전의 시간대에 예약하는지 확인
        if(request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "과거 시간대를 예약할 순 없습니다.");
        }

        // CartEntity에는 상품 정보 및 패키지 정보를 넣고, CartDetailEntity에는 상품에 대한 옵션을 집어넣음
        List<CartEntity> carts = cartConverter.convertToEntityByPackage(user, request);
        
        // DB에 저장
        List<CartEntity> savedCarts = cartService.addPackage(carts);

        // Response 객체 생성
        CartPackageResponse response = cartConverter.convertToDtoByPackage(savedCarts);

        return API.OK(response);
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
