package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>   {
//    @Query("SELECT c.cpProfile FROM CompanyEntity c WHERE c.cpId = :companyId")
//    String findProfileImageByCompanyId(@Param("companyId") Long companyId);
    CompanyEntity findByCpName(String name);
    List<CompanyEntity> findAllByCpId(Long id);
    Optional<CompanyEntity> findByCpEmail(String cpEmail);
    boolean existsByCpEmail(String cpEmail);
    boolean existsByCpMainContact(String cpMainContact);

}
