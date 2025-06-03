package aba3.lucid.chat_bot.service;

import aba3.lucid.chat_bot.repository.ChatBotRepository;
import aba3.lucid.domain.chatbot.converter.ChatBotConverter;
import aba3.lucid.domain.chatbot.dto.ChatBotResponse;
import aba3.lucid.domain.chatbot.entity.ChatBotEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotService {

    private final ChatBotRepository chatBotRepository;
    private final ChatBotConverter chatBotConverter;

    public List<ChatBotResponse> getChatListByParentId(Integer parentId) {
        List<ChatBotEntity> chatBotEntityList = chatBotRepository.findAllByParentId(parentId);
        return chatBotConverter.toResponseList(chatBotEntityList);
    }

}
