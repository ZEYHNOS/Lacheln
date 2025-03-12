package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.product.entity.OptionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionDetailRepository extends JpaRepository<OptionDetailEntity, Long> {
}
