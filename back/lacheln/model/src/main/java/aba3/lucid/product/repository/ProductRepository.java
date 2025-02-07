package aba3.lucid.product.repository;

import aba3.lucid.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Long, ProductEntity> {
}
