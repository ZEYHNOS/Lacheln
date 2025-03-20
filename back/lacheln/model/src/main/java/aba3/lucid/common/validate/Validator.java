package aba3.lucid.common.validate;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;

public class Validator {

    public static void throwIfInvalidId(long id) {
        if (id <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static void throwIfInvalidId(Long id) {
        if (id == null || id <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static void throwIfInvalidId(long id1, long id2) {
        if (id1 <= 0 || id2 <= 0) {
            throw new ApiException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public static void throwIfNull(Object obj) {
        if (obj == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }
    }

    public static void throwIfNull(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }
    }

}
