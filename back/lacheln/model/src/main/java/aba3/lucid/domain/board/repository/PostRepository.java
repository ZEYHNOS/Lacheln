package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // 삭제되지 않은 게시글 단건 조회
    Optional<PostEntity> findByPostIdAndDeletedFalse(Long postId);

    // 특정 게시판의 삭제되지 않은 게시글 전체 조회
    List<PostEntity> findAllByBoardAndDeletedFalse(BoardEntity board);

    // 자유/질문/리뷰 게시판 전체 조회 (삭제되지 않은 글만)
    List<PostEntity> findByBoard_BoardNameInAndDeletedFalse(List<String> boardNames);

    // 추천 수가 15 이상인 인기 게시글 조회 (삭제되지 않은 글만)
    @Query("""
        SELECT p FROM PostEntity p
        WHERE p.board.boardName IN :boardNames
        AND p.deleted = false
        AND SIZE(p.postLikeList) >= 15
    """)
    List<PostEntity> findPopularPosts(@Param("boardNames") List<String> boardNames);
}
