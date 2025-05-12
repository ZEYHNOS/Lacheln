package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatRoomEnterResponse {
    private List<ChatMessageDto> messages;
    private Long chatRoomId;
    private String userId;
    private String companyId;
    private String userName;
    private String companyName;
}
