package aba3.lucid.domain.chat.repository;

import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByUsersAndCompany(String userId, Long cpId);
}
