package aba3.lucid.domain.inquiry.dto;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import aba3.lucid.domain.inquiry.enums.ReportStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {

    private Long reportId;
    private LocalDateTime createdAt;
    private String reportTitle;
    private String reportContent;
    private ReportCategory reportCategory;
    private ReportStatus reportStatus;
    private String userId;
    private Long cpId;
    private String targetName;
    private String reporterName;
    private List<String> imageUrls;
}