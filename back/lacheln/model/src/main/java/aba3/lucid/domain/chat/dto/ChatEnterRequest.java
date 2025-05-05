package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatEnterRequest {
    private String userId;
    private Long companyId;
}
