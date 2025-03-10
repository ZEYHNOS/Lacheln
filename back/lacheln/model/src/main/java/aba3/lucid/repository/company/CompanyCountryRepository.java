package aba3.lucid.repository.company;


import aba3.lucid.domain.company.CompanyCountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyCountryRepository extends JpaRepository <CompanyCountryEntity, Long> {

}
