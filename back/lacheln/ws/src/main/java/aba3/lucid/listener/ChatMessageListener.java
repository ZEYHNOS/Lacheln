package aba3.lucid.listener;

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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageListener {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final ChatMessageConvertor chatMessageConvertor;
    private final SimpMessagingTemplate messagingTemplate; // WebSocket 전송용

    // 큐에 메시지가 들어올 시의 로직
    @RabbitListener(queues = "chat.queue")
    public void receive(ChatMessageDto messageDto, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        MessageEntity newMessage;
        try {
            // 1. 채팅방 검증 및 메시지 저장
            Optional<ChatRoomEntity> isChatRoom = chatRoomRepository.findById(messageDto.getChatRoomId());
            ChatRoomEntity chatRoom;
            
            // 찾아봤는데 채팅방이 없을 경우
            if(isChatRoom.isEmpty()) {
                throw new ApiException(ErrorCode.NOT_FOUND);
            } else {
                // 채팅방이 있는 경우
                chatRoom = isChatRoom.get();
                MessageEntity entity = chatMessageConvertor.convertToEntity(messageDto, chatRoom);
                newMessage = messageRepository.save(entity); // 메시지 저장하기
            }
            
            // 처음에는 메시지 ID가 null로 들어옴(DB에 저장할때 ID값 저장) 그래서 저장 후 ID값 set후 반환
            messageDto.setId(newMessage.getMsgId());

            // 2. WebSocket BroadCasting
            messagingTemplate.convertAndSend(
                    "/topic/chatroom." + chatRoom.getChatRoomId(),
                    messageDto
            );

            // 3. RabbitMq에 ACK신호 반환
            channel.basicAck(deliveryTag, false); // 완료되었으면 Queue에 Ack신호 전달하여 메시지 제거
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);

            // 메시지를 다시 큐에 넣고 재시도하도록 설정
            channel.basicNack(deliveryTag, false, true);
        }
    }
}