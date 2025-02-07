package aba3.lucid.company.repository;

import aba3.lucid.company.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Long, CompanyEntity> {
}
