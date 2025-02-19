package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialEnum {
    L("LOCAL"),
    K("KAKAO"),
    G("GOOGLE"),
    A("APPLE");

    private final String socialCode;
}
