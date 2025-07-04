package aba3.lucid.domain.chat.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.chat.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @Column(name = "msg_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msgId;

    // 채팅방ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoomId;

    // 메시지
    @Column(name = "msg_content", columnDefinition = "VARCHAR(255)", nullable = false)
    private String msgContent;

    // 전송시각
    @Column(name = "msg_send_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime msgSendTime;

    // 읽음여부
    @Enumerated(EnumType.STRING)
    @Column(name = "msg_read", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice msgRead;

    // 발신자 ID
    @Column(name = "msg_sender", columnDefinition = "CHAR(36)", nullable = false)
    private String msgSender;

    // 발신자 NAME
    @Column(name = "msg_sender_name", columnDefinition = "CHAR(36)", nullable = false)
    private String msgSenderName;

    // 수신자 ID
    @Column(name = "msg_receiver", columnDefinition = "CHAR(36)", nullable = false)
    private String msgReceiver;

    // 수신자 NAME
    @Column(name = "msg_receiver_name", columnDefinition = "CHAR(36)", nullable = false)
    private String msgReceiverName;

    public void changeMsgRead(BinaryChoice binaryChoice) {
        this.msgRead = binaryChoice;
    }
}
