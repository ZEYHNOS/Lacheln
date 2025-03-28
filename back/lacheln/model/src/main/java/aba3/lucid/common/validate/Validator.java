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


    public static void throwIfInvalidId(Long... ids) {
        for (Long id : ids) {
            if (id == null || id <= 0) {
                throw new ApiException(ErrorCode.INVALID_PARAMETER);
            }
        }
    }


    public static void assertStringValid(String s, int minimum, int maximum) {
        if (s == null || s.isBlank()) {
            throw new ApiException(ErrorCode.NULL_POINT, "문자열이 비어 있거나 null입니다.");
        }

        int length = s.length();
        if (length < minimum || length > maximum) {
            throw new ApiException(
                    ErrorCode.INVALID_PARAMETER,
                    String.format("문자열 길이가 맞지 않습니다. 최소 %d, 최대 %d", minimum, maximum)
            );
        }
    }

}
