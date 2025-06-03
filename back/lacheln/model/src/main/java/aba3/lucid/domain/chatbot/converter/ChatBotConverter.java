package aba3.lucid.domain.chatbot.converter;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.chatbot.dto.ChatBotResponse;
import aba3.lucid.domain.chatbot.entity.ChatBotEntity;

import java.util.List;

@Converter
public class ChatBotConverter {

    public ChatBotResponse toResponse(ChatBotEntity entity) {
        return ChatBotResponse.builder()
                .id(entity.getChatBotId())
                .content(entity.getContent())
                .build()
                ;
    }

    public List<ChatBotResponse> toResponseList(List<ChatBotEntity> entityList) {
        return entityList.stream()
                .map(this::toResponse)
                .toList()
                ;
    }

}
