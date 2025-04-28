package aba3.lucid.cart.controller;

import aba3.lucid.cart.business.CartBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.cart.dto.CartAddRequest;
import aba3.lucid.domain.cart.dto.CartAddResponse;
import aba3.lucid.domain.cart.dto.CartDeleteRequest;
import aba3.lucid.domain.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartBusiness cartBusiness;

    // 장바구니 검색
    @GetMapping("/search")
    public API<List<CartResponse>> searchCart()  {
        return cartBusiness.getCartList(AuthUtil.getUserId());
    }

    // 장바구니 담기
    @PostMapping("/add")
    public API<String> addCart(
            @RequestBody List<CartAddRequest> cartAddRequest
    )    {
        return cartBusiness.addCart(AuthUtil.getUserId(), cartAddRequest);
    }

    // 장바구니 제거
    @DeleteMapping("/delete")
    public API<String> deleteCart(
            @RequestBody CartDeleteRequest cartDeleteRequest
    )    {
        return cartBusiness.deleteCart(AuthUtil.getUserId(), cartDeleteRequest);
    }
}
