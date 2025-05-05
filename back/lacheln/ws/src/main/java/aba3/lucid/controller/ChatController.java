package aba3.lucid.controller;

import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send") // WebSocket 연결을 설정한 후 특정 메시지를 /chat.send 경로로 전송해야함
    public void sendMessage(@Payload ChatMessageDto message, Principal principal) {
//        만약 유저 정보가 필요하다면 이걸 사용하도록..
//        if (principal instanceof CustomAuthenticationToken token) {
//            CustomUserDetails userDetails = (CustomUserDetails) token.getPrincipal();
//        }

        message.sendAt(LocalDateTime.now());
        message.changeRead(BinaryChoice.N); // 최초엔 읽음 false
        rabbitTemplate.convertAndSend("chat.exchange", "chat.key", message);
        messagingTemplate.convertAndSend("/topic/chatroom." + message.getChatRoomId(), message);
    }
}
