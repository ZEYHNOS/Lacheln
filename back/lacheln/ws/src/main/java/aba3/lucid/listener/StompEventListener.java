package aba3.lucid.listener;

import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.service.ChatRoomSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final ChatRoomSessionService chatRoomSessionService;

    // 구독 이벤트 발생시 수행 로직
    @EventListener
    public void handleSessionSubscribe(SessionSubscribeEvent event)    {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Authentication auth = (Authentication) accessor.getUser();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
            log.info("인증되지 않았거나 구독하지 않은 사용자 입니다.");
            return;
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        String role = userDetails.getRole();
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination(); // ex: /sub/chat/room/123
        String userId = userDetails.getUserId(); // Long -> String
        Long companyId;

        if(role.equals("COMPANY"))  {
            companyId = userDetails.getCompanyId();
            chatRoomSessionService.addUserToRoom(companyId+"", destination, sessionId);
        } else {
            chatRoomSessionService.addUserToRoom(userId, destination, sessionId);
        }
    }

    // 연결 해제 이벤트 발생시 수행 로직 
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event)    {
        String sessionId = event.getSessionId();
        chatRoomSessionService.removeUserBySession(sessionId);
    }
}
