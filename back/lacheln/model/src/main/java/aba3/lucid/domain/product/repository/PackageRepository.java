package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.packages.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    @Query("SELECT p FROM PackageEntity p " +
            "WHERE p.packId = :packId " +
            "AND (p.packAdmin.id = :companyId OR p.packCompany1.id = :companyId OR p.packCompany2.id = :companyId)")
    Optional<PackageEntity> findByPackageIdAndCompanyId(long packId, long companyId);

    @Query("SELECT p FROM PackageEntity p " +
            "WHERE :companyId IN (p.packAdmin.cpId, p.packCompany1.cpId, p.packCompany2.cpId)")
    List<PackageEntity> findByCompanyIdInAnyRole(@Param("companyId") Long companyId);


}
