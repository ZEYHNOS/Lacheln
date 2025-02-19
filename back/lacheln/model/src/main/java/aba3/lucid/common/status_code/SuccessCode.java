package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements StatusCodeIfs{


    OK(200, 200, "요청 처리 성공"),

    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}
