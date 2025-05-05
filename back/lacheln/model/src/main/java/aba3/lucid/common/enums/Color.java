package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {

    RED("FF0000"),
    BLUE("0000FF"),
    BLACK("000000"),
    WHITE("FFFFFF"),
    ORANGE(""),
    YELLOW(""),
    GREEN(""),

    ;

    private final String hexCode;
}
