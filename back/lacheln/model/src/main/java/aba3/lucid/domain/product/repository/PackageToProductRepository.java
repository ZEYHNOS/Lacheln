package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageToProductRepository extends JpaRepository<PackageToProductEntity, Long> {

    boolean existsByPackageEntityAndProduct(PackageEntity packageEntity, ProductEntity product);

    @Query("SELECT COUNT(DISTINCT p.product) FROM PackageToProductEntity p " +
            "WHERE p.packageEntity.packId = :packId ")
    long countDistinctProductsByPackage(@Param("packId") long packId);

}
