package aba3.lucid.repository.product;

import aba3.lucid.domain.product.OptionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetailEntity, Long> {
}
