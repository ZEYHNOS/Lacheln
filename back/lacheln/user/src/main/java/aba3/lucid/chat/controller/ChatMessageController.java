package aba3.lucid.chat.controller;

import aba3.lucid.chat.business.ChatMessageBusiness;
import aba3.lucid.chat.service.ChatMessageService;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageBusiness chatMessageBusiness;

    @PutMapping("/{messageId}/read")
    public API<String> markAsRead(@RequestBody ChatMessageDto message) {
        return chatMessageBusiness.checkRead(message);
    }
}
