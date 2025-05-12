package aba3.lucid.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomAuthenticationToken;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.chat.dto.*;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import aba3.lucid.service.ChatRoomSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatController {

    private final RabbitTemplate rabbitTemplate;
    private final ChatRoomSessionService chatRoomSessionService;

    // 채팅방 입장, 없으면 채팅방 만들기(채팅방은 항상 소비자 측에서 만듬)
    @GetMapping("/addroom/{companyId}")
    public API<ChatRoomEnterResponse> enterOrAddRoom(@PathVariable String companyId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return chatRoomSessionService.enterOrAddRoom(customUserDetails.getUserId(), Long.parseLong(companyId));
    }

    // 채팅방에 있는 메시지를 가져오기
    @GetMapping("/messages/{chatRoomId}")
    public API<ChatRoomMessageResponse> getChatRoomMessages(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String chatRoomId) {
        return chatRoomSessionService.getMessageByRoom(user, chatRoomId);
    }

    // 채팅방 리스트 불러오기
    @GetMapping("/list")
    public API<ChatRoomListResponse> getChatRoomList(@AuthenticationPrincipal CustomUserDetails customUserDetails)  {
        return chatRoomSessionService.getRoomList(customUserDetails);
    }

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
