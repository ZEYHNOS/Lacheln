package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserCode implements StatusCodeIfs {

    USER_NOT_FOUND(400, 1404, "유저를 찾을 수 없습니다"),
    UN_AUTHORIZATION(403, 1403, "해당 권한이 없음"),
    CANNOT_FIND_DATA(405, 1405, "데이터를 입력해주세요.")

    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
