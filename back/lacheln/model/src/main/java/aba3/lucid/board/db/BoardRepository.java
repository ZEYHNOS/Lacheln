package aba3.lucid.board.db;

import aba3.lucid.board.model.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Integer, BoardEntity> {
}
