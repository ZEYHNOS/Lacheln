package aba3.lucid.domain.inquiry.convertor;



import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.domain.inquiry.entity.ReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportConvertor {
    public ReportEntity toReportEntity(ReportRequest request, Long cpId) {
        return ReportEntity.builder()
                .reportContent(request.getReportContent())
                .reportCategory(request.getReportCategory())
                .reportTitle(request.getReportTitle())
                .reportContent(request.getReportContent())
                .reportImageId(request.getImageUrls())
                .build();
    }

    public ReportResponse toReportResponse(ReportEntity reportEntity) {
        return ReportResponse.builder()
                .reportId(reportEntity.getReportId())
                .reportContent(reportEntity.getReportContent())
                .reportTitle(reportEntity.getReportTitle())
                .reportCategory(reportEntity.getReportCategory())
                .createdAt(reportEntity.getReportCreatedAt())
                .build();

    }
}
