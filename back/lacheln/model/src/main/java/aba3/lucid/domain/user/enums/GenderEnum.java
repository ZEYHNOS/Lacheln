package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    M("MALE"),
    F("FEMALE"),
    U("UNDEFINED");

    private final String gender;
}
