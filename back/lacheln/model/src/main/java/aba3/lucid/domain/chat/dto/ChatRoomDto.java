package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDto {
    private String roomId;
    private String userId;
    private String userName;
    private String companyId;
    private String companyName;
}
