package aba3.lucid.domain.inquiry.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportStatus {
    NEW("최근 신고"),
    CHECKED("확인 된 신고"),
    DONE("해결된 신고");
    ;
    private final String description;

}
