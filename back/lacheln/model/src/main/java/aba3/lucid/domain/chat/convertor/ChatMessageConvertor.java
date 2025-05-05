package aba3.lucid.domain.chat.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.chat.entity.MessageEntity;
import aba3.lucid.domain.chat.enums.UserType;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Converter
@RequiredArgsConstructor
public class ChatMessageConvertor {

    public MessageEntity convertToEntity(ChatMessageDto dto, ChatRoomEntity chatRoom)  {
        return MessageEntity
                .builder()
                .chatRoomId(chatRoom)
                .msgSender(dto.getUserType())
                .msgContent(dto.getMessage())
                .msgSendTime(LocalDateTime.now())
                .msgRead(dto.getRead())
                .build();
    }
}
