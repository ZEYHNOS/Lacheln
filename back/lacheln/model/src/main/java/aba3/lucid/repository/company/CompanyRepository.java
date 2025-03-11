package aba3.lucid.repository.company;

import aba3.lucid.domain.company.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>   {
    CompanyEntity findByCompanyName(String name);
}
