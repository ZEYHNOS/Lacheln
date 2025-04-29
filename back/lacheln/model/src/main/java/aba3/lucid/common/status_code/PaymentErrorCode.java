package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements StatusCodeIfs{

    INTERNAL_SERVER_ERROR(500, 100500, "test"),
    NO_PRODUCT_FOR_PAYMENT(400, 100401, "결제할 상품이 존재하지 않습니다."),

    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}
