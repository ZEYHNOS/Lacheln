package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.company.entity.CompanyAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAlertRepository extends JpaRepository<CompanyAlertEntity, Long> {

}
