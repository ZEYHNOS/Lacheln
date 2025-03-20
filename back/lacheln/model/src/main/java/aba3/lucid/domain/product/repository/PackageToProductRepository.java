package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageToProductRepository extends JpaRepository<PackageToProductEntity, Long> {
}
