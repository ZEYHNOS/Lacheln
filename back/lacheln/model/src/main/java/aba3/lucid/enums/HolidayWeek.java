package aba3.lucid.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HolidayWeek {

    ONE(""),
    TWO(""),
    THREE(""),
    FOUR(""),
    FIVE(""),
    ODD(""),
    EVEN("")
    ;

    private final String description;
}
