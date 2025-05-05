package aba3.lucid.chat.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.chat.dto.ChatEnterRequest;
import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomEntity findByIdWithThrow(Long roomId)    {
        return chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));
    }

    public ChatRoomEntity findByUserIdAndCpId(String userId, Long companyId)    {
        return chatRoomRepository.findByUsersAndCompany(userId, companyId);
    }

    public ChatRoomEntity save(ChatEnterRequest request) {
        ChatRoomEntity newRoom = ChatRoomEntity
                .builder()
                .users(request.getUserId())
                .company(request.getCompanyId())
                .build();
        return chatRoomRepository.save(newRoom);
    }
}
