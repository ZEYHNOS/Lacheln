package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements StatusCodeIfs{

    NO_PRODUCT_FOR_PAYMENT(400, 100401, "결제할 상품이 존재하지 않습니다."),
    CANCEL(400, 100402, "결제가 취소되었습니다."),
    BAD_REQUEST(400, 100403, "잘못된 결제 요청")
    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}
