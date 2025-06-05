package aba3.lucid.domain.chat.repository;

import aba3.lucid.domain.chat.entity.ChatRoomEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findByUsersAndCompany(String userId, Long cpId);
    List<ChatRoomEntity> findAllByUsers(String userId);
    List<ChatRoomEntity> findAllByCompany(Long userId);
}
