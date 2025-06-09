package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
    WHITE(""),
    BLACK(""),
    RED(""),
    ORANGE(""),
    YELLOW(""),
    GREEN(""),
    BLUE(""),
    NAVY(""),
    PURPLE(""),
    BEIGE(""),
    PINK(""),


    ;

    private final String hexCode;
}
