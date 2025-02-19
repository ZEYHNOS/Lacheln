package aba3.lucid.domain.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String description;
}
