package aba3.lucid.chat_bot.controller;

import aba3.lucid.chat_bot.service.ChatBotService;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.chatbot.dto.ChatBotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat-bot")
@RequiredArgsConstructor
public class ChatBotController {

    private final ChatBotService chatBotService;

    @GetMapping("/{parentId}")
    public API<List<ChatBotResponse>> getChatListByParentId(
            @PathVariable Integer parentId
    ) {
        List<ChatBotResponse> chatBotResponseList = chatBotService.getChatListByParentId(parentId);
        return API.OK(chatBotResponseList);
    }

}
