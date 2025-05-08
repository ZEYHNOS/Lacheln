package aba3.lucid.domain.inquiry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportCategory {

    BAD_SERVICE("업체의 서비스가 좋지 않습니다."),
    RESTRICTED("제한된 컨텐츠"),
    GENERAL("일반 컨텐츠"),
    FLAGGED("선정성이 있는 컨텐츠");

    ;

    private final String description;
}
