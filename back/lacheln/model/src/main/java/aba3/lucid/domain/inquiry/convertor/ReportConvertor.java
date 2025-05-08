package aba3.lucid.domain.inquiry.convertor;



import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.inquiry.dto.ReportImageRequest;
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
public class ReportConvertor {
    public ReportEntity toReportEntity(ReportRequest request, CompanyEntity company, UsersEntity user) {
        return ReportEntity.builder()
                .reportContent(request.getReportContent())
                .reportCategory(request.getReportCategory())
                .reportTitle(request.getReportTitle())
                .reportContent(request.getReportContent())
                .reportImageId(request.getImageUrls())
                .company(company)
                .user(user)
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


//    ReportImageRequest 리스트 +  ReportEntity를
//      ReportImageEntity 리스트로 바꿔줌

    public List<ReportImageEntity> toImagesEntities(List<ReportImageRequest> imageRequests, ReportEntity report) {
        if(imageRequests == null) return Collections.emptyList();
        return imageRequests.stream()
                .map(r-> ReportImageEntity.builder()
                        .report(report)
                        .reportImageUrl(r.getUrl())
                        .build())
                .collect(Collectors.toList());

    }


//    ?  ReportEntity에 연결된 엔티티 리스트를 이미지 응답 DTO로 변환
//     */

    public List<ReportImageResponse> toImageResponses(
            List<ReportImageEntity> imageEntities
    ) {
        if (imageEntities == null) return Collections.emptyList();
        return imageEntities.stream()
                .map(e -> ReportImageResponse.builder()
                        .imageId(e.getReportImageId())
                        .url(e.getReportImageUrl())
                        .build())
                .collect(Collectors.toList());
    }



}
