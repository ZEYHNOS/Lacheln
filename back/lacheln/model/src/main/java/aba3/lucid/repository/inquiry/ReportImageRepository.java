package aba3.lucid.repository.inquiry;

import aba3.lucid.domain.inquiry.ReportImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportImageRepository extends JpaRepository <ReportImageEntity, Long> {

}
