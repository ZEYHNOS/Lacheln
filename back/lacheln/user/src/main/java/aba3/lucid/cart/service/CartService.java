package aba3.lucid.cart.service;

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
    public List<CartEntity> getCartByUserIdWithThrow(String userId) {
        return cartRepository.findAllByUsers_UserId(userId);
    }

    // 장바구니 추가
    public CartEntity addCart(CartEntity cart)    {
        return cartRepository.save(cart);
    }

    public List<CartEntity> findAllById(List<Long> cartId) {
        return cartRepository.findAllById(cartId);
    }

    // 장바구니 목록 n개 제거
    @Transactional
    public void removeCart(List<Long> cartIds) {
        for(Long id : cartIds) {
            cartRepository.deleteByCartId(id);
        }
    }

    // 장바구니 목록 전부 제거
    @Transactional
    public void removeAllCart(String userId) {
        cartRepository.deleteAllByUsers_UserId(userId);
    }
}
