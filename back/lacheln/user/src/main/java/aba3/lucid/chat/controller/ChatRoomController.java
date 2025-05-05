package aba3.lucid.chat.controller;

import aba3.lucid.chat.business.ChatRoomBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.chat.dto.ChatEnterRequest;
import aba3.lucid.domain.chat.dto.ChatEnterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomBusiness chatRoomBusiness;

    @PostMapping("/add")
    public API<ChatEnterResponse> add(@RequestBody ChatEnterRequest request) {
        return chatRoomBusiness.addRoom(request);
    }

    
}
