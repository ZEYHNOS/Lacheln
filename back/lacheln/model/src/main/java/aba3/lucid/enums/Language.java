package aba3.lucid.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {

    KOR("대한민국"),
    JPN("일본"),
    USA("미국"),
    CHA("중국"),

    ;

    private final String countryName;
}
