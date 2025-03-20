package aba3.lucid.domain.product.studio.repository;

import aba3.lucid.domain.product.enums.ProductStatus;
import aba3.lucid.domain.product.studio.entity.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends JpaRepository<StudioEntity, Long> {

    List<StudioEntity> findAllByCompany_CpIdAndPdStatus(long cpId, ProductStatus status);

    List<StudioEntity> findAllByCompany_CpIdAndPdStatusNot(long cpId, ProductStatus status);

}
