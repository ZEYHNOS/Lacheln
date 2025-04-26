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

    // 장바구니 목록에서 제거
    @Transactional
    public void removeCart(String userId, Long cartId) {
        cartRepository.deleteByCartIdAndUsers_UserId(cartId, userId);
    }
}
