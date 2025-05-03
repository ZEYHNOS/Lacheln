package aba3.lucid.domain.company.repository;


import aba3.lucid.domain.company.entity.AdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdjustmentRepository extends JpaRepository <AdjustmentEntity, Long> {
    Optional<AdjustmentEntity> findByCompany_CpId(Long cpId);

}
