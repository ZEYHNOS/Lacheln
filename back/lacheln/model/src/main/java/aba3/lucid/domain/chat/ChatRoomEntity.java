package aba3.lucid.domain.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_cp_chattingroom")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    //업체ID 자동증가
    private Long cpId;

    //소비자ID 이메일+비밀번호+비밀키(암호화시켜서저장)
    private String userId;
}
