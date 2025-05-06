package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.product.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    @Query("SELECT p.pdImageUrl FROM ProductImageEntity p WHERE p.product.pdId = :productId")
    List<String> findImageUrlsByProductId(@Param("productId") Long productId);

    void deleteByProduct_PdId(Long productId);

}
