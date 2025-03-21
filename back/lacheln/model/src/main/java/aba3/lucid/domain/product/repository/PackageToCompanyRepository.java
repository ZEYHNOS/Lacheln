package aba3.lucid.domain.product.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.packages.entity.PackageEntity;
import aba3.lucid.domain.packages.entity.PackageToCompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageToCompanyRepository extends JpaRepository<PackageToCompanyEntity, Long> {

    // 현재 패키지ID에 소속된 인원 수
    long countByPackageEntity_PackId(long packId);

    Optional<PackageToCompanyEntity> findByPackageEntityAndCompany(PackageEntity packageEntity, CompanyEntity companyEntity);
}
