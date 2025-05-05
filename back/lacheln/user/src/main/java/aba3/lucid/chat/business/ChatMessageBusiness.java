package aba3.lucid.chat.business;

import aba3.lucid.chat.service.ChatMessageService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import aba3.lucid.domain.chat.entity.MessageEntity;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ChatMessageBusiness {

    private final ChatMessageService chatMessageService;

    public API<String> checkRead(ChatMessageDto message) {
        chatMessageService.checkRead(message.getMessageId());
        return API.OK("읽음 처리 완료");
    }
}
