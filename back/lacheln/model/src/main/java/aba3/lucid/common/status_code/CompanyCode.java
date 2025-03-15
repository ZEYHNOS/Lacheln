package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyCode implements StatusCodeIfs {

    ALREADY_REGISTERED_COMPANY(400, 50000, "이미 가입된 회원입니다."),
    INVALID_EMAIL_FORMAT(400,5000,"잘못된 이메일 형식입니다."),
    MEMBER_NOT_FOUND(400,5000,"해당 회원을 찾을 수 없습니다."),

    TMP(1234, 1234, "뭘 넣어야할까나"),

    ;

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
