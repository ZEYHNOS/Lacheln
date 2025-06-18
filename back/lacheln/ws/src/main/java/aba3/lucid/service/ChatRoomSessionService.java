package aba3.lucid.service;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.CustomUserDetails;
import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.chat.convertor.ChatMessageConvertor;
import aba3.lucid.domain.chat.dto.*;
import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.chat.entity.MessageEntity;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import aba3.lucid.domain.chat.repository.MessageRepository;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomSessionService {

    private final ChatMessageConvertor convertor;
    private final RedisTemplate<String, String> redisTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UsersRepository usersRepository;
    private final CompanyRepository companyRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String ROOM_PREFIX = "chat:room:";           // roomId -> Set<userId>
    private static final String SESSION_PREFIX = "chat:session:";     // sessionId -> userId
    private static final String USER_ROOM_PREFIX = "chat:user-room:"; // userId -> roomId

    public API<ChatRoomEnterResponse> enterOrAddRoom(String userId, Long companyId) {
        UsersEntity user = usersRepository.findById(userId).orElse(null);
        CompanyEntity company = companyRepository.findById(companyId).orElse(null);

        if (user == null || company == null) {
            throw new ApiException(ErrorCode.GONE);
        }

        Optional<ChatRoomEntity> room = chatRoomRepository.findByUsersAndCompany(userId, companyId);
        ChatRoomEnterResponse response;

        if (room.isPresent()) {
            List<MessageEntity> messages = messageRepository.findByChatRoomId_ChatRoomIdOrderByMsgSendTime(room.get().getChatRoomId());
            for (MessageEntity entity : messages) {
                entity.changeMsgRead(BinaryChoice.Y);
            }
            List<MessageEntity> savedMessages = messageRepository.saveAll(messages);

            response = ChatRoomEnterResponse.builder()
                    .messages(convertor.convertToDtoList(savedMessages))
                    .chatRoomId(room.get().getChatRoomId())
                    .userId(userId)
                    .userName(user.getUserName())
                    .companyId(companyId + "")
                    .companyName(company.getCpName())
                    .build();

            return API.OK(response, "기존 채팅방으로 진입합니다.");
        } else {
            ChatRoomEntity newRoom = ChatRoomEntity.builder()
                    .company(companyId)
                    .users(userId)
                    .build();
            chatRoomRepository.save(newRoom);

            response = ChatRoomEnterResponse.builder()
                    .chatRoomId(newRoom.getChatRoomId())
                    .userId(userId)
                    .userName(user.getUserName())
                    .companyId(companyId + "")
                    .companyName(company.getCpName())
                    .build();

            return API.OK(response, "채팅방을 새로 개설하였습니다!");
        }
    }

    public API<ChatRoomListResponse> getRoomList(CustomUserDetails user) {
        List<ChatRoomEntity> rooms;
        List<ChatRoomDto> responses = new ArrayList<>();
        String role = user.getRole();

        if ("USER".equals(role)) {
            String userId = user.getUserId();
            rooms = chatRoomRepository.findAllByUsers(userId);
            responses = rooms.stream().map(chatRoomEntity -> ChatRoomDto.builder()
                            .roomId(chatRoomEntity.getChatRoomId() + "")
                            .companyId(chatRoomEntity.getCompany() + "")
                            .companyName(companyRepository.findById(chatRoomEntity.getCompany()).orElse(null).getCpName())
                            .userId(chatRoomEntity.getUsers())
                            .userName(usersRepository.findById(chatRoomEntity.getUsers()).orElse(null).getUserName())
                            .unreadCount(messageRepository.countUnreadMessagesByChatRoomId(chatRoomEntity.getChatRoomId(), userId))
                            .build())
                    .toList();
        } else if ("COMPANY".equals(role)) {
            Long companyId = user.getCompanyId();
            rooms = chatRoomRepository.findAllByCompany(companyId);
            responses = rooms.stream().map(chatRoomEntity -> ChatRoomDto.builder()
                            .roomId(chatRoomEntity.getChatRoomId() + "")
                            .companyId(chatRoomEntity.getCompany() + "")
                            .companyName(companyRepository.findById(chatRoomEntity.getCompany()).orElse(null).getCpName())
                            .userId(chatRoomEntity.getUsers())
                            .userName(usersRepository.findById(chatRoomEntity.getUsers()).orElse(null).getUserName())
                            .unreadCount(messageRepository.countUnreadMessagesByChatRoomId(chatRoomEntity.getChatRoomId(), companyId))
                            .build())
                    .toList();
        }

        return API.OK(ChatRoomListResponse.builder().chatRooms(responses).build());
    }

    public API<ChatRoomMessageResponse> getMessageByRoom(CustomUserDetails user, String roomId) {
        if (roomId == null) {
            throw new ApiException(ErrorCode.GONE);
        }

        List<Long> readMessages = new ArrayList<>();
        List<MessageEntity> messages = messageRepository.findByChatRoomId_ChatRoomIdOrderByMsgSendTime(Long.parseLong(roomId));

        for (MessageEntity messageEntity : messages) {
            if (messageEntity.getMsgRead().equals(BinaryChoice.N)) {
                String receiver = user.getUserId() != null ? user.getUserId() : user.getCompanyId() + "";
                if (messageEntity.getMsgReceiver().equals(receiver)) {
                    messageEntity.changeMsgRead(BinaryChoice.Y);
                }
            }
            readMessages.add(messageEntity.getMsgId());
            messageRepository.save(messageEntity);
        }

        if (!readMessages.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/read." + roomId,
                    ChatReadResponse.builder().messageIds(readMessages).build());
        }

        List<ChatMessageDto> response = messages.stream().map(msg -> ChatMessageDto.builder()
                        .messageId(msg.getMsgId())
                        .sendAt(msg.getMsgSendTime())
                        .read(msg.getMsgRead())
                        .message(msg.getMsgContent())
                        .chatRoomId(msg.getChatRoomId().getChatRoomId())
                        .receiverId(msg.getMsgReceiver())
                        .receiverName(msg.getMsgReceiverName())
                        .senderId(msg.getMsgSender())
                        .senderName(msg.getMsgSenderName())
                        .build())
                .toList();

        return API.OK(ChatRoomMessageResponse.builder().messages(response).build());
    }

    // ✅ 유저를 Redis에 등록 (단 하나의 방만 참여하도록)
    public void addUserToRoom(String userId, String destination, String sessionId) {
        String newRoomId = extractRoomId(destination);

        // 이전 방 제거
        String oldRoomId = redisTemplate.opsForValue().get(USER_ROOM_PREFIX + userId);
        if (oldRoomId != null && !oldRoomId.equals(newRoomId)) {
            redisTemplate.opsForSet().remove(ROOM_PREFIX + oldRoomId, userId);
            redisTemplate.delete(USER_ROOM_PREFIX + userId);
            log.info("기존 방 {}에서 사용자 {} 제거", oldRoomId, userId);
        }

        // 새 방 등록
        redisTemplate.opsForSet().add(ROOM_PREFIX + newRoomId, userId);
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, userId);
        redisTemplate.opsForValue().set(USER_ROOM_PREFIX + userId, newRoomId);

        log.info("사용자 {}를 방 {}에 등록 (세션: {})", userId, newRoomId, sessionId);
    }

    // ✅ 세션 종료 시 유저 Redis에서 제거
    public void removeUserBySession(String sessionId) {
        String userId = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        if (userId != null) {
            String roomId = redisTemplate.opsForValue().get(USER_ROOM_PREFIX + userId);
            if (roomId != null) {
                redisTemplate.opsForSet().remove(ROOM_PREFIX + roomId, userId);
                redisTemplate.delete(USER_ROOM_PREFIX + userId);
                log.info("사용자 {}가 방 {}에서 나감 (세션 종료)", userId, roomId);
            }
            redisTemplate.delete(SESSION_PREFIX + sessionId);
        }
    }

    // ✅ 유저가 특정 방에 있는지 확인
    public boolean isUserInRoom(String userId, String roomId) {
        log.info("ROOM_PREFIX + roomId : {}", ROOM_PREFIX + "read." + roomId);
       return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(ROOM_PREFIX + "read." + roomId, userId));
    }

    // ✅ destination으로부터 roomId 추출
    private String extractRoomId(String destination) {
        return destination.substring(destination.lastIndexOf("/") + 1);
    }
}
