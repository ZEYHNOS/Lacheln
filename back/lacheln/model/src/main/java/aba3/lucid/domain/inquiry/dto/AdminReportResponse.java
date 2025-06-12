package aba3.lucid.domain.inquiry.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminReportResponse {
    private Long reportId;
    private String reporterId;
    private String reporterName;
    private String reporterEmail;
    private String reportedId;
    private String reportedName;
    private String reportedEmail;

}
