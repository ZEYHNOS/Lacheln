package aba3.lucid.chat.business;

import aba3.lucid.chat.service.ChatRoomService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.api.API;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.chat.dto.ChatEnterRequest;
import aba3.lucid.domain.chat.dto.ChatEnterResponse;
import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.chat.repository.ChatRoomRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ChatRoomBusiness {

    private final ChatRoomService chatRoomService;

    public API<ChatEnterResponse> addRoom(ChatEnterRequest request) {
        ChatRoomEntity isThere = chatRoomService.
                findByUserIdAndCpId(request.getUserId(), request.getCompanyId());
        if(isThere == null) {
            ChatRoomEntity room = chatRoomService.save(request);
            ChatEnterResponse response = ChatEnterResponse
                    .builder()
                    .roomId(room.getChatRoomId())
                    .userId(room.getUsers())
                    .companyId(room.getCompany())
                    .build();
            return API.OK(response);
        } else {
            throw new ApiException(ErrorCode.IT_ALREADY_EXISTS, "이미 존재하는 방이 있습니다.");
        }
    }
}
