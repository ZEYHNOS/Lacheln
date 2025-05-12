package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatRoomMessageResponse {
    List<ChatMessageDto> messages;
}
