package aba3.lucid.domain.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

    U("사용자"),
    C("업체")
    ;

    private final String description;
}
