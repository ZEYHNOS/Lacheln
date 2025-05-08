package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.enums.ReportCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportImageResponse {
    private long imageId;
    private String url;
}