package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>   {
    //CompanyEntity findByCpName(String name);

    Optional<CompanyEntity> findByCpEmail(String cpEmail);
    boolean existsByCpEmail(String cpEmail);
}
