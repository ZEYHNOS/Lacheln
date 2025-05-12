package aba3.lucid.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatReadResponse {
    private List<Long> messageIds;
}
