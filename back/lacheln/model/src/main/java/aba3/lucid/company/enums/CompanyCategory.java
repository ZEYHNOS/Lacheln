package aba3.lucid.company.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyCategory {

    S("스튜디오"),
    D("드레스"),
    M("메이크"),

    ;

    private final String description;
}
