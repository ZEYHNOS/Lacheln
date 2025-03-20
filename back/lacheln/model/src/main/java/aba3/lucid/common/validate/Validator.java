package aba3.lucid.common.validate;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;

public class Validator {

    public static void throwIfNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new ApiException(ErrorCode.NULL_POINT);
            }
        }
    }


    public static void throwIfInvalidId(long... ids) {
        for (long id : ids) {
            if (id <= 0) {
                throw new ApiException(ErrorCode.INVALID_PARAMETER);
            }
        }
    }
}
