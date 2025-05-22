package aba3.lucid.domain.cart.repository;

import aba3.lucid.domain.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findAllByUsers_UserId(String userId);
    void deleteByCartId(Long cartId);
    void deleteAllByUsers_UserId(String userId);
    CartEntity findByUsers_UserIdAndProductId(String userId, Long productId);
}
