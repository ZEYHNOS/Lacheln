package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageEnum {
    KOR("kor"),  // 한국어
    USA("eng"),  // 영어
    JPN("jpn"),  // 일본어
    CHN("zho");  // 중국어

    private final String languageCode;
}
