package aba3.lucid.common.status_code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailCode implements StatusCodeIfs {

    ALREADY_REGISTERED(400, 50000, "이미 가입된 회원입니다."),
    VERIFY_CODE_EXPIRED(400, 5000   , "만료된 인증 번호 입니다."),
    INVALID_CODE(400, 500, "유효하지 않은 인증번호 입니다.");

    private final Integer httpStatusCode;
    private final Integer serverStatusCode;
    private final String description;
}
