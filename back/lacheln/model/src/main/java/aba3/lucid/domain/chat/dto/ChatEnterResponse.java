package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatEnterResponse {
    private Long roomId;
    private String userId;
    private Long companyId;
}
