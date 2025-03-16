package aba3.lucid.domain.product.dress.repository;

import aba3.lucid.domain.product.dress.entity.DressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DressRepository extends JpaRepository<DressEntity, Long> {

    // 상품 리스트 가지고 오기
    List<DressEntity> findAllByCompany_CpId(long companyId);

}
