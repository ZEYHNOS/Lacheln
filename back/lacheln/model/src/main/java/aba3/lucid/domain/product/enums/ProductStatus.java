package aba3.lucid.domain.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    ACTIVE("활성화"),
    INACTIVE("비활성화"),
    PACKAGE("패키지 전용"),
    REMOVE("삭제"),

    ;


    private final String description;
}
