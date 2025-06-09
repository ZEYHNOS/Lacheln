package aba3.lucid.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum SmsEnum {
    CODE_NOT_FOUND("해당 번호로 전송된 인증코드가 없습니다."),
    ALREADY_EXISTS("이미 등록된 전화번호 입니다."),
    CODE_NOT_MATCHED("인증 코드가 일치하지 않습니다."),
    SEND_SUCCESS("전송이 성공적으로 완료되었습니다. 핸드폰 문자메시지에 전송된 인증번호를 입력해주세요."),
    VERIFY_SUCCESS("인증이 성공적으로 완료되었습니다!");

    private final String description;
}
