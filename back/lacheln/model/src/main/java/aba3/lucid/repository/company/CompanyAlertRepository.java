package aba3.lucid.repository.company;

import aba3.lucid.domain.company.CompanyAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAlertRepository extends JpaRepository<CompanyAlertEntity, Long> {

}
