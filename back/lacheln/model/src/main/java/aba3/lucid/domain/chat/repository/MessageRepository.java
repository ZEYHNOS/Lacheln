package aba3.lucid.domain.chat.repository;

import aba3.lucid.domain.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChatRoomId_ChatRoomIdOrderByMsgSendTime(Long chatRoomId);

    // 방 이름과 수신자 ID를 조회하여 읽지않은 메시지 수 return(사용자)
    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.chatRoomId.chatRoomId = :roomId AND m.msgReceiver = :userId AND m.msgRead = 'N' ")
    Long countUnreadMessagesByChatRoomId(
            @Param("roomId") Long roomId,
            @Param("userId") String userId);

    // 방 이름과 수신자 ID를 조회하여 읽지않은 메시지 수 return(업체)
    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.chatRoomId.chatRoomId = :roomId AND m.msgReceiver = :cpId AND m.msgRead = 'N' ")
    Long countUnreadMessagesByChatRoomId(
            @Param("roomId") Long roomId,
            @Param("cpId") Long cpId);
}
