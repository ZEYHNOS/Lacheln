package aba3.lucid.common.exception;

import aba3.lucid.common.status_code.StatusCodeIfs;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionIfs {

    private final StatusCodeIfs errorCode;
    private final String errorDescription;

    public ApiException(StatusCodeIfs errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCode = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }

    // 추가로 description 에 적고 싶을 때
    public ApiException(StatusCodeIfs errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
        this.errorDescription = description;
    }

}
