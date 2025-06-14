package aba3.lucid.domain.company.repository;

import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>   {
    // 기존 월별 조회
    @Query("SELECT FUNCTION('MONTH', c.companyJoinDate) as month, COUNT(c) FROM CompanyEntity c GROUP BY FUNCTION('MONTH', c.companyJoinDate)")
    List<Object[]> countMonthlyJoin();

    // 오늘 가입한 회사 수 조회
    long countByCompanyJoinDateBetween(LocalDateTime start, LocalDateTime end);

    // 또는 날짜만으로 조회하려면
    @Query("SELECT COUNT(c) FROM CompanyEntity c WHERE DATE(c.companyJoinDate) = CURRENT_DATE")
    long countTodayJoinedCompanies();
    CompanyEntity findByCpName(String name);
    List<CompanyEntity> findAllByCpId(Long id);
    Optional<CompanyEntity> findByCpEmail(String cpEmail);
    boolean existsByCpEmail(String cpEmail);
    boolean existsByCpMainContact(String cpMainContact);

}
