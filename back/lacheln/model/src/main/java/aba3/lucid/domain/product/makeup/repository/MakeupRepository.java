package aba3.lucid.domain.product.makeup.repository;

import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeupRepository extends JpaRepository<MakeupEntity, Long> {
    List<MakeupEntity> findAllByCompany_cpId(long companyId);
}
