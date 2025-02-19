package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyCode implements StatusCodeIfs {

    TMP(1234, 1234, "뭘 넣어야할까나"),

    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
