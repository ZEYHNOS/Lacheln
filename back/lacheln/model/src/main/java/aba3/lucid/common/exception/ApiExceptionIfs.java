package aba3.lucid.common.exception;

import aba3.lucid.common.status_code.StatusCodeIfs;

public interface ApiExceptionIfs {

    StatusCodeIfs getErrorCode();

    String getErrorDescription();

}
