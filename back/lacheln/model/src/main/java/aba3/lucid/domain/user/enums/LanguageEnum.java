package aba3.lucid.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageEnum {
    KOR("KOR"),  // 한국어
    ENG("USA"),  // 영어
    JPN("JPN"),  // 일본어
    ZHO("CHN");  // 중국어

    private final String languageCode;
}
