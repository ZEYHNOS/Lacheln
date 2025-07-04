package aba3.lucid.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewStatus {
    REGISTERED ("등록됐습니다"),
    UPDATED ("수정됐습니다"),
    DELETED ("삭제됐습니다"),
    REPLY_NEEDED("등록이 필요합니다.")
    ;
    private final String description;

}
