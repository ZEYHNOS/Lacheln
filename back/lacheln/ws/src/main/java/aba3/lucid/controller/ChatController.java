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
}
