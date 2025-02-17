package aba3.lucid.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportCategory {

    BAD_SERVICE("업체의 서비스가 좋지 않습니다."),

    ;

    private final String description;
}
