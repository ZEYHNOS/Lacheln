package aba3.lucid.rabbitmq;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.chat.convertor.ChatMessageConvertor;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.chat.entity.MessageEntity;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import aba3.lucid.domain.chat.repository.MessageRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatMessageConvertor chatMessageConvertor;

    // 업체가 수신하는 큐에 메시지가 들어올 시의 로직
    @RabbitListener(queues = "chat.queue")
    public void receive(ChatMessageDto messageDto, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            ChatRoomEntity chatRoom = chatRoomRepository
                    .findById(messageDto.getChatRoomId())
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));
            MessageEntity entity = chatMessageConvertor.convertToEntity(messageDto, chatRoom); // 컨버팅 및 채팅방 입력
            messageRepository.save(entity); // 메시지 저장하기
            channel.basicAck(deliveryTag, false); // 완료되었으면 Queue에 Ack신호 전달하여 메시지 제거
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);

            // 메시지를 다시 큐에 넣고 재시도하도록 설정
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
