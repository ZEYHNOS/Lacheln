package aba3.lucid.chat.service;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import aba3.lucid.domain.chat.entity.MessageEntity;
import aba3.lucid.domain.chat.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void checkRead(Long messageId) {
        MessageEntity
                message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않은 메시지 입니다."));
        message.changeMsgRead(BinaryChoice.Y);
        messageRepository.save(message);
    }
}
