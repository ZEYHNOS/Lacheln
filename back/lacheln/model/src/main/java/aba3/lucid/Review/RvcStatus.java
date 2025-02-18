package aba3.lucid.Review;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RvcStatus {
    VISIBLE("표시"),
    HIDDEN("숨기기"),
    DELETED("삭제")
    ;
    private final String description;

}
