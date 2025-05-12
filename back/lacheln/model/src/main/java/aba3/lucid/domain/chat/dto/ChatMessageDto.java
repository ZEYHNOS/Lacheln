package aba3.lucid.domain.chat.dto;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.chat.enums.UserType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class ChatMessageDto {
    private Long messageId;
    private Long chatRoomId;
    private String message;
    private BinaryChoice read;
    private String senderId;
    private String receiverId;
    private LocalDateTime sendAt;
    private String senderName;
    private String receiverName;

    @JsonCreator
    public ChatMessageDto(@JsonProperty("messageId") Long messageId,
                          @JsonProperty("chatRoomId") Long chatRoomId,
                          @JsonProperty("message") String message,
                          @JsonProperty("read") BinaryChoice read,
                          @JsonProperty("senderId") String senderId,
                          @JsonProperty("receiverId") String receiverId,
                          @JsonProperty("sendAt") LocalDateTime sendAt,
                          @JsonProperty("senderName") String senderName,
                          @JsonProperty("receiverName") String receiverName
        ) {
        this.messageId = messageId;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.read = read;
        this.sendAt = sendAt;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public void changeRead(BinaryChoice read)   { this.read = read; }
    public void sendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }
    public void setId(Long messageId) { this.messageId = messageId; }
}
