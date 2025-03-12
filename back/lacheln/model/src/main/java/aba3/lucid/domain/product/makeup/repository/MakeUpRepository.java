package aba3.lucid.domain.product.makeup.repository;

import aba3.lucid.domain.product.makeup.entity.MakeUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakeUpRepository extends JpaRepository<MakeUpEntity, Long> {
}
