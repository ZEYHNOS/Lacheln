package aba3.lucid.domain.inquiry.dto;

import aba3.lucid.domain.inquiry.entity.ReportImageEntity;
import aba3.lucid.domain.inquiry.enums.ReportCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    @NotBlank
    private String reportTitle;

    @NotBlank
    private String reportContent;

    @NotNull
    private ReportCategory reportCategory;

    @NotBlank
    @JsonProperty("reportTarget")
    private String reportTarget;

    private Long cpId;       // 업체 신고일 경우
    private String userId;   // // 회원 신고일 경우

    // 클라이언트에서 업로드한 이미지 URL 리스트
    private List<String> imageUrls;

}
