package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActiveEnum {

    ACTIVE("활성화"),
    INACTIVE("비활성화");
    private final String description;

}
