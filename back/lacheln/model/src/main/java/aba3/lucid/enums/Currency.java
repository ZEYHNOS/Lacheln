package aba3.lucid.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {

    OWN("원"),

    ;

    private final String currency;
}
