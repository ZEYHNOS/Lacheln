package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TierEnum {
    AMATEUR("아마추어"),
    SEMI_PRO("세미프로"),
    PROFESSIONAL("프로페셔널"),
    WORLD_CLASS("월드클래스"),
    CHALLENGER("챌린저"),
    ADMIN("admin");

    private final String tier;
}
