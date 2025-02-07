package aba3.lucid.productimage.repository;

import aba3.lucid.productimage.model.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<Long, ProductImageEntity> {

}