package aba3.lucid.domain.chat;

import aba3.lucid.domain.company.CompanyEntity;
import aba3.lucid.domain.user.UsersEntity;
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
