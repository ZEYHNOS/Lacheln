package aba3.lucid.report.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.inquiry.entity.ReportEntity;
import aba3.lucid.domain.inquiry.entity.ReportImageEntity;
import aba3.lucid.domain.inquiry.repository.ReportImageRepository;
import aba3.lucid.domain.inquiry.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportImageRepository reportImageRepository;


    @Transactional
    public ReportEntity submitReport(ReportEntity entity) {
        ReportEntity savedEntity = reportRepository.save(entity);
        List<ReportImageEntity> images = entity.getReportImages();
        if(images != null && !images.isEmpty()) {
            images.forEach(img-> img.setReport(savedEntity));
            reportImageRepository.saveAll(images);
        }
        return savedEntity;
    }



    public List<ReportImageEntity> saveImages(List<ReportImageEntity> images) {
        return reportImageRepository.saveAll(images);
    }

    public ReportEntity findByIdWithThrow(long reportId) {
        return reportRepository.findById(reportId).orElseThrow(()
                -> new ApiException(ErrorCode.NOT_FOUND));
    }


    //자기 자신을 신고할 수 없다
    public void validateSelfReport(String reporterId, String reportedId) {
        if(reportedId.equals(reporterId)) {
            throw new ApiException(ErrorCode.CANNOT_REPORT_YOURSELF);
        }
    }
    public long getTodayReportCount() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();  // 다음날 00:00:00

        long todayReportCount = reportRepository.countByReportCreatedAtBetween(startOfDay, endOfDay);
        return todayReportCount;
    }


}
