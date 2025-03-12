package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>   {
    CompanyEntity findByCompanyName(String name);
}
