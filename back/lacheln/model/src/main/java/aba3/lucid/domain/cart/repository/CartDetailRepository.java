package aba3.lucid.domain.cart.repository;

import aba3.lucid.domain.cart.entity.CartDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Long> {
}
