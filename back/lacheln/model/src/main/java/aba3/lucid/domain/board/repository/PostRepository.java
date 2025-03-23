package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    /**
     * 특정 게시판에 속한 모든 게시글을 조회
     */
    List<PostEntity> findAllByBoard(BoardEntity board);

    // 자유/질문/리뷰 게시판에 속한 글만 조회
    List<PostEntity> findByBoard_BoardNameIn(List<String> boardNames);
}
