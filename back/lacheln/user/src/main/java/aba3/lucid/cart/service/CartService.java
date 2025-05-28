package aba3.lucid.cart.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.cart.entity.CartEntity;
import aba3.lucid.domain.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    // 유저 아이디로 장바구니 조회
    public List<CartEntity> getCartByUserId(String userId) {
        return cartRepository.findAllByUsers_UserId(userId);
    }
    
    // 장바구니 ID로 장바구니 한개 조회
    public CartEntity findByIdWithThrow(Long cartId)   {
        return cartRepository.findById(cartId).orElseThrow(() ->
                new ApiException(ErrorCode.NULL_POINT, "해당하는 장바구니가 존재하지 않습니다.")
        );
    }
    
    // 장바구니 추가 및 갱신
    public CartEntity addCart(CartEntity cart)    {
        return cartRepository.save(cart);
    }

    // ID로 카트 모두 조회
    public List<CartEntity> findAllById(List<Long> cartId) {
        return cartRepository.findAllById(cartId);
    }

    // 장바구니 목록 n개 제거
    @Transactional
    public void removeCart(List<Long> cartIds) {
        cartRepository.deleteAllById(cartIds);
    }

    // 장바구니 목록 전부 제거
    @Transactional
    public void removeAllCart(String userId) {
        cartRepository.deleteAllByUsers_UserId(userId);
    }
}
