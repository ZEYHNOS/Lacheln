package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements StatusCodeIfs {
    // 상품 10000번대

    PRODUCT_NOT_FOUND(404, 10404, "상품을 찾을 수 없습니다."),

    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
