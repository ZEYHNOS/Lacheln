package aba3.lucid.domain.product.dress.repository;

import aba3.lucid.domain.product.dress.entity.DressSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DressSizeRepository extends JpaRepository<DressSizeEntity, Long> {
}
