package aba3.lucid.domain.inquiry.dto;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {


    private Long reportId;
    private LocalDateTime createdAt;
    private String reportTitle;
    private String reportContent;
    private ReportCategory reportCategory;
    private List<String> imageUrls;
}