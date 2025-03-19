package aba3.lucid.domain.product.makeup.repository;

import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.makeup.entity.MakeupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakeupRepository extends JpaRepository<MakeupEntity, Long> {

    // 활성화된 상품만 조회 (유저용)
    List<MakeupEntity> findAllByCompany_CpIdAndPdStatus(long companyId, ProductStatus pdStatus);

    // 삭제되지 않은 모든 상품 조회 (업체용)
    List<MakeupEntity> findAllByCompany_CpIdAndPdStatusNot(long companyId, ProductStatus pdStatus);
}
