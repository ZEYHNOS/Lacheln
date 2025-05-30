package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Optional<BoardEntity> findByBoardName(String boardName);
}
