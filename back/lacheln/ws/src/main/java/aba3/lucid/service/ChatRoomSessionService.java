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
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
//TODO ※ 매우 중요 현재는 모듈이 분할이 안되어서 그냥 두지만 모듈 분할 후에는 해당 서비스 절대적으로 각 was에 만들어줘야함 ※ //
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

    private static final String ROOM_PREFIX = "chat:room:";         // roomId -> userId Set
    private static final String SESSION_PREFIX = "chat:session:";   // sessionId -> userId

    // 유저와 업체를 조회하여 채팅방이 있다면 해당 채팅방으로 접근, 없다면 채팅방을 만들기
    public API<ChatRoomEnterResponse> enterOrAddRoom(String userId, Long companyId) {
        
        // 유저 및 업체조회
        UsersEntity user = usersRepository.findById(userId).orElse(null);
        CompanyEntity company = companyRepository.findById(companyId).orElse(null);

        // 예외 처리
        if(user == null || company == null) {
            throw new ApiException(ErrorCode.GONE);
        }
        
        // 방 조회
        Optional<ChatRoomEntity> room = chatRoomRepository.findByUsersAndCompany(userId, companyId);
        ChatRoomEnterResponse response;
        
        // 방이 존재한다면
        if(room.isPresent()) {
            // 방에 존재하는 메시지들 전부 가져와서
            List<MessageEntity> messages = messageRepository.findByChatRoomId_ChatRoomIdOrderByMsgSendTime(room.get().getChatRoomId());
            
            // 읽음 처리로 변경
            for (MessageEntity entity : messages) {
                entity.changeMsgRead(BinaryChoice.Y);
            }
            
            // 후 저장
            List<MessageEntity> savedMessages = messageRepository.saveAll(messages);
            
            // Response 생성
            response = ChatRoomEnterResponse
                    .builder()
                    .messages(convertor.convertToDtoList(savedMessages))
                    .chatRoomId(room.get().getChatRoomId())
                    .userId(userId)
                    .userName(user.getUserName())
                    .companyId(companyId+"")
                    .companyName(company.getCpName())
                    .build();
            return API.OK(response, "기존 채팅방으로 진입합니다.");
        } else {
            ChatRoomEntity newRoom = ChatRoomEntity
                    .builder()
                    .company(companyId)
                    .users(userId)
                    .build();
            chatRoomRepository.save(newRoom);
            response = ChatRoomEnterResponse
                    .builder()
                    .chatRoomId(newRoom.getChatRoomId())
                    .userId(userId)
                    .userName(user.getUserName())
                    .companyId(companyId+"")
                    .companyName(company.getCpName())
                    .build();
            return API.OK(response, "채팅방을 새로 개설하였습니다!");
        }
    }

    // 현재 세션을 통해 채팅방 리스트 가져오기
    public API<ChatRoomListResponse> getRoomList(CustomUserDetails user)    {
        List<ChatRoomEntity> rooms;
        List<ChatRoomDto> responses = new ArrayList<>();
        String role = user.getRole();
        String userId;
        Long companyId;

        if(role.equals("USER")) {
            userId = user.getUserId();
            rooms = chatRoomRepository.findAllByUsers(userId);
            responses = rooms
                    .stream()
                    .map(chatRoomEntity -> ChatRoomDto
                            .builder()
                            .roomId(chatRoomEntity.getChatRoomId() + "")
                            .companyId(chatRoomEntity.getCompany() + "")
                            .companyName(companyRepository.findById(chatRoomEntity.getCompany()).orElse(null).getCpName())
                            .userId(chatRoomEntity.getUsers())
                            .userName(usersRepository.findById(chatRoomEntity.getUsers()).orElse(null).getUserName())
                            .unreadCount(messageRepository.countUnreadMessagesByChatRoomId(chatRoomEntity.getChatRoomId(), userId))
                            .build())
                    .toList();
        } else if(role.equals("COMPANY"))   {
            companyId = user.getCompanyId();
            rooms = chatRoomRepository.findAllByCompany(companyId);
            responses = rooms
                    .stream()
                    .map(chatRoomEntity -> ChatRoomDto
                            .builder()
                            .roomId(chatRoomEntity.getChatRoomId() + "")
                            .companyId(chatRoomEntity.getCompany() + "")
                            .companyName(companyRepository.findById(chatRoomEntity.getCompany()).orElse(null).getCpName())
                            .userId(chatRoomEntity.getUsers())
                            .userName(usersRepository.findById(chatRoomEntity.getUsers()).orElse(null).getUserName())
                            .unreadCount(messageRepository.countUnreadMessagesByChatRoomId(chatRoomEntity.getChatRoomId(), companyId))
                            .build())
                    .toList();
        }


        return API.OK(ChatRoomListResponse.builder()
                        .chatRooms(responses).build());
    }
    
    // 특정 채팅방에 있는 메시지들 가져오기
    public API<ChatRoomMessageResponse> getMessageByRoom(CustomUserDetails user, String roomId)  {
        if(roomId == null) {
            throw new ApiException(ErrorCode.GONE);
        }

        List<Long> readMessages = new ArrayList<>();
        List<MessageEntity> messages;
        messages = messageRepository.findByChatRoomId_ChatRoomIdOrderByMsgSendTime(Long.parseLong(roomId));

        for (MessageEntity messageEntity : messages) {
            if(messageEntity.getMsgRead().equals(BinaryChoice.N)) {
                if (user.getUserId() != null) {
                    if (messageEntity.getMsgReceiver().equals(user.getUserId())) {
                        messageEntity.changeMsgRead(BinaryChoice.Y);
                    }
                } else if (user.getCompanyId() != null) {
                    if (messageEntity.getMsgReceiver().equals(user.getCompanyId() + "")) {
                        messageEntity.changeMsgRead(BinaryChoice.Y);
                    }
                }
            } else {
                continue;
            }
            readMessages.add(messageEntity.getMsgId());
            messageRepository.save(messageEntity);
        }

        // ✅ 읽음 처리된 메시지 ID들을 상대방에게 실시간 브로드캐스팅
        if (!readMessages.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/read." + roomId, ChatReadResponse.builder().messageIds(readMessages).build());
        }

        List<ChatMessageDto> response = messages
                .stream()
                .map(MessageEntity -> ChatMessageDto
                        .builder()
                        .messageId(MessageEntity.getMsgId())
                        .sendAt(MessageEntity.getMsgSendTime())
                        .read(MessageEntity.getMsgRead())
                        .message(MessageEntity.getMsgContent())
                        .chatRoomId(MessageEntity.getChatRoomId().getChatRoomId())
                        .receiverId(MessageEntity.getMsgReceiver())
                        .receiverName(MessageEntity.getMsgReceiverName())
                        .senderId(MessageEntity.getMsgSender())
                        .senderName(MessageEntity.getMsgSenderName())
                        .build())
                .toList();

        return API.OK(ChatRoomMessageResponse.builder().messages(response).build());
    }
    
    // 이 아래는 Redis쪽 부분임
    // redis에 유저등록
    public void addUserToRoom(String userId, String destination, String sessionId) {
        String roomId = extractRoomId(destination);

        // 유저 접속 정보 저장
        redisTemplate.opsForSet().add(ROOM_PREFIX + roomId, userId);
        redisTemplate.opsForValue().set(SESSION_PREFIX + sessionId, userId);
    }

    // 유저 redis에서 제거
    public void removeUserBySession(String sessionId) {
        String userId = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        if (userId != null) {
            redisTemplate.delete(SESSION_PREFIX + sessionId);

            // 모든 방을 순회하면서 유저 제거
            Set<String> keys = redisTemplate.keys(ROOM_PREFIX + "*");
            if (keys != null) {
                for (String key : keys) {
                    redisTemplate.opsForSet().remove(key, userId);
                }
            }
        }
    }

    // 유저가 현재 방에 있는지 확인
    public boolean isUserInRoom(String userId, String roomId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(ROOM_PREFIX  + "chatroom." + roomId, userId)
        );
    }

    // destination을 통한 방 아이디 추출
    private String extractRoomId(String destination) {
        return destination.substring(destination.lastIndexOf("/") + 1);
    }
}

