package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements StatusCodeIfs {

    BAD_REQUEST(400, 400, "잘못된 요청입니다."),
    UNAUTHORIZED(401, 401, "권한이 없습니다."),
    PAYMENT_REQUIRED(402, 402, "결제가 필요합니다."),
    FORBIDDEN(403, 403, "지정된 리소스에 대한 접근이 금지되었습니다."),
    NOT_FOUND(404, 404, "찾을 수 없습니다."),
    REQUEST_TIMEOUT(408, 408, "처리하는 시간이 오래 걸립니다."),
    GONE(404, 410, "현재 존재하지 않는 리소스입니다."),
    TOO_MANY_REQUESTS(400, 429, "짧은 시간동안 너무 많은 요청을 보내셨습니다."),
    IT_ALREADY_EXISTS(407, 407, "이미 존재합니다."),
    FAILED_SEND_CODE(408, 408, "SMS 전송에 실패하였습니다!"),
    CODE_NOT_MATCH(409, 409, "검증 코드가 일치하지않습니다!"),

    SERVER_ERROR(500, 500, "서버에서 에러가 발생했습니다."),

    NULL_POINT(400, 400, "존재하지 않습니다."),
    INVALID_PARAMETER(400, 4001, "잘못된 파라미터 값입니다."),
    REQUIRED_FIELD_MISSING(400, 4002, "필수 값이 누락되었습니다."),
    USER_BANNED(400, 400, "사용자가 차단됐습니다"),
    CANNOT_REPORT_YOURSELF(400, 400, "신고할 수 없습니다"),
    RESTRICTED_CONTENT(403, 403, "신고 내용이 잘 못 됩니다"),
    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
