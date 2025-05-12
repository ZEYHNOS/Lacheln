package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    List<ProductEntity> findAllByDeleteDateBefore(LocalDateTime now);

    List<ProductEntity> findAllByHashtagList_TagName(String tagName);

    List<ProductEntity> findAllByCompany_CpIdAndPdStatusNot(long cpId, ProductStatus status);

}
