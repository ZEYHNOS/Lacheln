package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements StatusCodeIfs{


    OK(200, 200, "요청 처리 성공"),
    DELETE_TOKEN(201, 201, "토큰을 제거하였습니다."),
    SESSION_VALID(202, 202, "세션 검증에 성공하였습니다!"),
    RESET_CONTENT(205, 205, "화면을 리셋해주세요")

    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;

}
