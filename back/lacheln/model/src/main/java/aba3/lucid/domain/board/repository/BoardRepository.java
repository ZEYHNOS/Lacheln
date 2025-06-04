package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    Optional<BoardEntity> findByBoardName(String boardName);

    // ✅ 중복 검사 최적화용 existsBy 메서드 추가
    boolean existsByBoardName(String boardName);
}
