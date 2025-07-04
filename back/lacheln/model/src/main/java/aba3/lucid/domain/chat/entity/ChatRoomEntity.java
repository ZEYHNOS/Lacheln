package aba3.lucid.domain.chat.entity;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "chatting_room")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomEntity {

    // 채팅방 ID
    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    //업체ID
    @Column(name = "cp_id", nullable = false)
    private Long company;

    //소비자ID
    @Column(name = "user_id", nullable = false)
    private String users;

    // 메시지들
    @OneToMany(mappedBy = "chatRoomId", cascade = CascadeType.ALL)
    private List<MessageEntity> messages;
}
