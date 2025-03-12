package aba3.lucid.domain.company.repository;


import aba3.lucid.domain.company.entity.CompanyCountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCountryRepository extends JpaRepository <CompanyCountryEntity, Long> {

}
