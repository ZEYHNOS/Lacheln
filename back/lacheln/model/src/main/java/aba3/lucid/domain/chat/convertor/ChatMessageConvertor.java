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
import java.util.List;

@Converter
@RequiredArgsConstructor
public class ChatMessageConvertor {

    public MessageEntity convertToEntity(ChatMessageDto dto, ChatRoomEntity chatRoom)  {
        return MessageEntity
                .builder()
                .chatRoomId(chatRoom)
                .msgSender(dto.getSenderId()+"")
                .msgReceiver(dto.getReceiverId()+"")
                .msgContent(dto.getMessage())
                .msgSendTime(LocalDateTime.now())
                .msgRead(dto.getRead())
                .msgSenderName(dto.getSenderName())
                .msgReceiverName(dto.getReceiverName())
                .build();
    }

    public ChatMessageDto convertToDto(Long chatRoomId, MessageEntity messageEntity) {
        return ChatMessageDto
                .builder()
                .chatRoomId(chatRoomId)
                .senderId(messageEntity.getMsgSender())
                .receiverId(messageEntity.getMsgReceiver())
                .messageId(messageEntity.getMsgId())
                .read(messageEntity.getMsgRead())
                .sendAt(messageEntity.getMsgSendTime())
                .senderName(messageEntity.getMsgSenderName())
                .receiverName(messageEntity.getMsgReceiverName())
                .build();
    }

    public ChatMessageDto convertToDto2(MessageEntity messageEntity) {
        return ChatMessageDto.builder()
                .chatRoomId(messageEntity.getChatRoomId().getChatRoomId())
                .senderId(messageEntity.getMsgSender())
                .senderName(messageEntity.getMsgSenderName())
                .receiverName(messageEntity.getMsgReceiverName())
                .receiverId(messageEntity.getMsgReceiver())
                .messageId(messageEntity.getMsgId())
                .sendAt(messageEntity.getMsgSendTime())
                .message(messageEntity.getMsgContent())
                .read(messageEntity.getMsgRead())
                .build();
    }

    public List<ChatMessageDto> convertToDtoList(List<MessageEntity> messageEntityList) {
        return messageEntityList.stream()
                .map(this::convertToDto2)
                .toList();
    }
}