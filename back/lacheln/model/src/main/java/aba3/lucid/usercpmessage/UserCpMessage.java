package aba3.lucid.usercpmessage;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "user_cp_message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCpMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ucMessageId;

    //채팅방ID
    private Long chatRoomId;

    //업체ID 자동증가
    private Long cpId;

    //소비자ID 이메일+비밀번호+비밀키(암호화시켜서저장)
    private String userId;

    //메시지
    private String ucContent;

    //전송시간
    private LocalTime ucSendTime;

    //읽음여부
    private String ucRead;

    //발신자식별코드
    private String ucSenderCode;
}
