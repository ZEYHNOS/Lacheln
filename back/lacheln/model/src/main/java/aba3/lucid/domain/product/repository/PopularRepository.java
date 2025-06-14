package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.product.entity.PopularEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRepository extends JpaRepository<PopularEntity, Long> {
}
