package aba3.lucid.domain.chat.entity;

import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity cpId;

    //소비자ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity userId;
}
