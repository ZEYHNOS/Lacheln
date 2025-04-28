package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements StatusCodeIfs{

    INTERNAL_SERVER_ERROR(500, 100500, "test")
    ;


    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}
