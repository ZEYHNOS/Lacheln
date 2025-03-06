package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Schedules {
    EXPECTED("예정"),
    PROGRESS("진행중"),
    COMPLETE("완료"),
    CANCEL("취소")
    ;

    private final String schedules;
}
