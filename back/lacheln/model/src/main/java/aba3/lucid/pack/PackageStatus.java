package aba3.lucid.pack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PackageStatus {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    DELETE("삭제")
    ;

    private final String description;
}
