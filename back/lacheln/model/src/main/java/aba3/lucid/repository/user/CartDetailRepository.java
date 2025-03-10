package aba3.lucid.repository.user;

import aba3.lucid.domain.user.CartDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Long> {
}
