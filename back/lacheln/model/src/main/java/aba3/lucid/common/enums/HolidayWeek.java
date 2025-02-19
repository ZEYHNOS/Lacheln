package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayWeek {

    ONE("1주차"),
    TWO("2주차"),
    THREE("3주차"),
    FOUR("4주차"),
    FIVE("5주차"),
    ODD("홀수"),
    EVEN("짝수")
    ;

    private final String description;
}
