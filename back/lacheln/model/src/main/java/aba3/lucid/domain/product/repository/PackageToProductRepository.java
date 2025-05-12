package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToProductEntity;
import aba3.lucid.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageToProductRepository extends JpaRepository<PackageToProductEntity, Long> {

    boolean existsByPackageEntity_packIdAndCpId(Long packageId, Long companyId);

    @Query("SELECT COUNT(DISTINCT p.product) FROM PackageToProductEntity p " +
            "WHERE p.packageEntity.packId = :packId ")
    long countDistinctProductsByPackage(@Param("packId") Long packId);

    List<PackageToProductEntity> findAllByPackageEntity_packId(Long packageId);

    Optional<PackageToProductEntity> findByPackageEntity_PackIdAndCpId(Long packId, Long cpId);

}
