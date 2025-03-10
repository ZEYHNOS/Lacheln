package aba3.lucid.repository.product;

import aba3.lucid.domain.product.DressSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DressSizeRepository extends JpaRepository<DressSizeEntity, Long> {
}
