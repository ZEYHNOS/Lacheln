package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.alert.entity.CompanyAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyAlertRepository extends JpaRepository<CompanyAlertEntity, Long> {

    List<CompanyAlertEntity> findAllByCompany_CpId(Long companyId);

}
