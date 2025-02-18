package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    M("MALE"),
    F("FEMALE");

    private final String gender;
}
