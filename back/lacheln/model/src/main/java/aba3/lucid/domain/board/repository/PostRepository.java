package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * 자유, 질문, 리뷰 게시판 중 추천수 15 이상인 게시글만 조회
     * (인기 게시판 조회용)
     */
    @Query("SELECT p FROM PostEntity p " +
            "WHERE p.board.boardName IN :boardNames " +
            "AND (SELECT COUNT(pl) FROM PostLikeEntity pl WHERE pl.post = p) >= 15 " +
            "ORDER BY (SELECT COUNT(pl) FROM PostLikeEntity pl WHERE pl.post = p) DESC")
    List<PostEntity> findPopularPosts(@Param("boardNames") List<String> boardNames);
}
