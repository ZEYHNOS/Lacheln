package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements StatusCodeIfs {
    // 상품 10000번대

    PRODUCT_NOT_FOUND(404, 10404, "상품을 찾을 수 없습니다."),
    NO_PRODUCT_OWNERSHIP(400, 10400, "해당 상품의 소유 업체가 아닙니다."),
    PRODUCT_NOT_PRIVATE(400, 10401, "상태가 비공개가 아닙니다."),
    PRODUCT_SNAPSHOT_MISMATCH(400, 10402, "상품 정보가 현재와 일치하지 않습니다."),

    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
