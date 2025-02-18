package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TierEnum {
    CHALLENGER("챌린저"),
    WORLD_CLASS("월드클래스"),
    PROFESSIONAL("프로페셔널"),
    SEMI_PRO("세미프로"),
    AMATEUR("아마추어"),
    ADMIN("admin");

    private final String tier;
}
