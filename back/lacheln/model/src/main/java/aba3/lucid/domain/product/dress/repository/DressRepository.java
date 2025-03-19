package aba3.lucid.domain.product.dress.repository;

import aba3.lucid.domain.product.dress.entity.DressEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DressRepository extends JpaRepository<DressEntity, Long> {

    // 활성화된 상품만 조회 (유저용)
    List<DressEntity> findAllByCompany_CpIdAndPdStatus(long companyId, ProductStatus pdStatus);

    // 삭제되지 않은 모든 상품 조회 (업체용)
    List<DressEntity> findAllByCompany_CpIdAndPdStatusNot(long companyId, ProductStatus pdStatus);


}
