package aba3.lucid.domain.product.studio.repository;

import aba3.lucid.domain.product.studio.entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudioRepository extends JpaRepository<StudioEntity, Long> {
}
