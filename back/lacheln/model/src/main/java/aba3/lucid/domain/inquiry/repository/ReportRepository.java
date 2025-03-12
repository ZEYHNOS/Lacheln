package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository <ReportEntity, Long> {
}
