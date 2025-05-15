package aba3.lucid.domain.inquiry.dto;


import aba3.lucid.domain.inquiry.entity.ReportEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportImageRequest {
    private long reportId;
    @NotBlank
    private String url;
}
