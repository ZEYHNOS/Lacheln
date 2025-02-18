package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialEnum {
    LOCAL("L"),
    KAKAO("K"),
    GOOGLE("G"),
    APPLE("A");

    private final String socialCode;
}
