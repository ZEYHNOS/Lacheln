package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.company.enums.CompanyCategory;
import aba3.lucid.domain.product.entity.ProductEntity;
import aba3.lucid.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    List<ProductEntity> findAllByDeleteDateBefore(LocalDateTime now);

    List<ProductEntity> findAllByHashtagList_TagName(String tagName);

    List<ProductEntity> findAllByCompany_CpIdAndPdStatusNot(Long cpId, ProductStatus status);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            ":category = p.company.cpCategory AND " +
            "p.pdPrice >= :minimum AND " +
            "p.pdPrice <= :maximum AND " +
            "p.pdStatus = :status")
    Page<ProductEntity> filteringProducts(
            @Param("category") CompanyCategory category,
            @Param("minimum") Integer minimum,
            @Param("maximum") Integer maximum, Pageable pageable,
            @Param("status") ProductStatus status);
}
