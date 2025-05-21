package aba3.lucid.domain.product.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewCommentStatus {
    VISIBLE("표시"),
    HIDDEN("숨기기"),
    DELETED("삭제"),
    CREATED("생성")
    ;
    private final String description;

}
