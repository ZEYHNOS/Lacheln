package aba3.lucid.cart.controller;

import aba3.lucid.cart.business.CartBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.domain.cart.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/cart")
@Tag(name = "Cart Controller", description = "장바구니 관련 API")
public class CartController {

    private final CartBusiness cartBusiness;

    // 장바구니 검색
    @GetMapping("/search")
    @Operation(summary = "사용자 장바구니 조회", description = "해당하는 소비자 장바구니의 상품을 조회합니다.")
    public API<List<CartResponse>> searchCart(
            @AuthenticationPrincipal CustomUserDetails user
            )  {
        return cartBusiness.getCartList(user.getUserId());
    }

    // 장바구니 업데이트
    @PutMapping("/update")
    @Operation(summary = "사용자 장바구니 업데이트", description = "해당하는 소비자 장바구니의 상품을 갱신합니다.")
    public API<CartUpdateResponse> updateCart(
            @RequestBody CartUpdateRequest request
    )   {
        return cartBusiness.cartUpdate(request);
    }

    // 장바구니 담기
    @PostMapping("/add")
    @Operation(summary = "사용자 장바구니 추가", description = "해당하는 소비자의 장바구니에 상품을 추가합니다.")
    public API<CartAddResponse> addCart(
            @RequestBody CartAddRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    )    {
        return cartBusiness.addCart(user.getUserId(), request);
    }

    // 장바구니 제거
    @DeleteMapping("/delete")
    @Operation(summary = "사용자 장바구니 제거", description = "해당하는 소비자의 장바구니에 상품을 제거합니다.")
    public API<String> deleteCart(
            @RequestBody CartDeleteRequest cartDeleteRequest
    )    {
        return cartBusiness.deleteCart(cartDeleteRequest);
    }

    // 장바구니 비우기
    @DeleteMapping("/pay/success")
    @Operation(summary = "사용자 장바구니 결제 완료시 상품 제거", description = "장바구니에 있는 상품들을 결제완료했을때 장바구니를 비웁니다.")
    public API<String> paySuccess(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return cartBusiness.deleteAllCart(user.getUserId());
    }
}