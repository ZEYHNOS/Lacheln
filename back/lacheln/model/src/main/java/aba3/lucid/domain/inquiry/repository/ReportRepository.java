package aba3.lucid.domain.inquiry.repository;

import aba3.lucid.domain.inquiry.entity.ReportEntity;
import aba3.lucid.domain.inquiry.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository <ReportEntity, Long> {

    long countByReportCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<ReportEntity> getReportEntitiesByReportId(Long reportId);
    int countByReportStatus(ReportStatus status);
    List<ReportEntity> findByReportStatus(ReportStatus status);
}
