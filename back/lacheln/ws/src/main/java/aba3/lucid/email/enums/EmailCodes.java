package aba3.lucid.email.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum EmailCodes {
    SUCCESS("이메일이 성공적으로 인증되었습니다!"),
    NOT_CORRECT("잘못된 인증코드 입니다."),
    EXPIRED("인증번호가 만료되었거나 존재하지 않습니다."),
    SEND_SUCCESS("인증번호 전송완료."),
    ALREADY_EXIST("이미 존재하는 이메일 입니다.");

    private final String description;
}
