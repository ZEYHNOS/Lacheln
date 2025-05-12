package aba3.lucid.domain.chat.repository;

import aba3.lucid.domain.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChatRoomId_ChatRoomIdOrderByMsgSendTime(Long chatRoomId);
}
