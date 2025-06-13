package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TierEnum {
    AMATEUR("아마추어", 0),
    SEMI_PRO("세미프로", 1),
    PROFESSIONAL("프로페셔널", 2),
    WORLD_CLASS("월드클래스", 3),
    CHALLENGER("챌린저", 4),
    ADMIN("admin", Integer.MAX_VALUE);

    private final String tier;
    private final int rank;
}
