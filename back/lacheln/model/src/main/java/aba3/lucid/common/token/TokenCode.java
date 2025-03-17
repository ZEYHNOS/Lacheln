package aba3.lucid.common.token;

import aba3.lucid.common.exception.ApiExceptionIfs;
import aba3.lucid.common.status_code.StatusCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenCode implements StatusCodeIfs {

    TOKEN_NOT_FOUND(400, 1404, "token is not found");

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
