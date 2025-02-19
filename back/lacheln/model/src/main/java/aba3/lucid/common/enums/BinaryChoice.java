package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BinaryChoice {

    Y("yes"),
    N("no")
    ;

    private final String description;

}
