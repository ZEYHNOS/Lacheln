package aba3.lucid.chat_bot.repository;

import aba3.lucid.domain.chatbot.entity.ChatBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotRepository extends JpaRepository<ChatBotEntity, Integer> {

    List<ChatBotEntity> findAllByParentId(Integer parentId);

}
