package aba3.lucid.domain.inquiry.converter;



import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.inquiry.dto.ReportImageResponse;
import aba3.lucid.domain.inquiry.dto.ReportRequest;
import aba3.lucid.domain.inquiry.dto.ReportResponse;
import aba3.lucid.domain.inquiry.entity.ReportEntity;
import aba3.lucid.domain.inquiry.entity.ReportImageEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReportConverter {
    public ReportEntity toReportEntity(ReportRequest request, CompanyEntity company, UsersEntity user) {
        //ReportEntity 본체 생성
      ReportEntity report = ReportEntity.builder()
                .reportTarget(request.getReportTarget())
                .reportContent(request.getReportContent())
                .reportCategory(request.getReportCategory())
                .reportTitle(request.getReportTitle())
                 .reporterName(request.getReporterName())
                 .targetName(request.getTargetName())
                .company(company)
                .user(user)
                .build();

        //imageUrls -> ReportImageEntity 변환
        if(request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ReportImageEntity> images = request.getImageUrls().stream()
                    .map(url -> ReportImageEntity.builder()
                            .reportImageUrl(url)
                            .report(report)
                            .build())
                    .collect(Collectors.toList());
            report.setReportImages(images);
        }
        return  report;
    }

    public ReportResponse toReportResponse(ReportEntity reportEntity) {
        return ReportResponse.builder()
                .userId(reportEntity.getUser().getUserId())
                .cpId(reportEntity.getCompany().getCpId())
                .reporterName(reportEntity.getReporterName())
                .targetName(reportEntity.getTargetName())
                .reportId(reportEntity.getReportId())
                .reportContent(reportEntity.getReportContent())
                .reportTitle(reportEntity.getReportTitle())
                .reportCategory(reportEntity.getReportCategory())
                .createdAt(reportEntity.getReportCreatedAt())
                .reportStatus(reportEntity.getReportStatus())
                .build();

    }


    public List<ReportImageEntity> toImageEntities(
            List<String> imageUrls,
            ReportEntity report
    ) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Collections.emptyList();
        }

        return imageUrls.stream()
                .map(url -> ReportImageEntity.builder()
                        .report(report)
                        .reportImageUrl(url)
                        .build())
                .collect(Collectors.toList());
    }




    public ReportImageResponse toImageResponse(ReportImageEntity reportImageEntity) {
        return ReportImageResponse.builder()
                .imageId(reportImageEntity.getReportImageId())
                .url(reportImageEntity.getReportImageUrl())
                .build();
    }

}
