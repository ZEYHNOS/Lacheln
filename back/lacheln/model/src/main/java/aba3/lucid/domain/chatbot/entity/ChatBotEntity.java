package aba3.lucid.domain.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_bot")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatBotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatBotId;

    private Integer parentId;

    private String content;

}
