package aba3.lucid.controller;

import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.chat.dto.ChatMessageDto;
import aba3.lucid.service.ChatRoomSessionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

// Hidden 어노테이션은 swagger에서 무시하게 만드는 어노테이션
@Hidden
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatRoomSessionService chatRoomSessionService;

    // 연결된 WebSocket으로부터 해당하는 요청이 올 경우 수행하는 로직(해당 메서드는 메시지를 전송하여 BroadCasting함)
    @MessageMapping("/send") // WebSocket 연결을 설정한 후 특정 메시지를 /chat/send 경로로 전송해야함
    public void sendMessage(@Payload ChatMessageDto message, Principal principal) {
        boolean isRecipientOnline = false;

        // 유저 정보가 필요하면 꺼냄
        if (principal instanceof CustomAuthenticationToken token) {
            CustomUserDetails userDetails = (CustomUserDetails) token.getPrincipal();
            // 1. 상대방 온라인 여부 Redis에서 조회
            if(userDetails.getRole().equals("USER")) {
                isRecipientOnline = chatRoomSessionService.isUserInRoom(message.getReceiverId()+"", message.getChatRoomId()+"");
            } else if(userDetails.getRole().equals("COMPANY")) {
                isRecipientOnline = chatRoomSessionService.isUserInRoom(message.getReceiverId(), message.getChatRoomId() +"");
            }
        }

        // 현재 시간 세팅 및 Redis를 통해 조회된 온라인 조회 여부로 읽음 여부 세팅
        message.sendAt(LocalDateTime.now());
        message.changeRead(isRecipientOnline ? BinaryChoice.Y : BinaryChoice.N);

        // RabbitMq chat이라는 queue에 메시지 전달
        rabbitTemplate.convertAndSend("chat.exchange", "chat.key", message);
    }
}
