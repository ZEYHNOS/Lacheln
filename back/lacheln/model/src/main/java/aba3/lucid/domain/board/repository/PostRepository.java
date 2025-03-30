package aba3.lucid.domain.board.repository;

import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    /**
     * 게시글 ID로 삭제되지 않은 게시글 단건 조회
     */
    Optional<PostEntity> findByPostIdAndDeletedFalse(Long postId);

    /**
     * 특정 게시판(BoardEntity)의 삭제되지 않은 게시글 전체 조회
     */
    List<PostEntity> findAllByBoardAndDeletedFalse(BoardEntity board);

    /**
     * 자유/질문/리뷰 게시판의 삭제되지 않은 게시글 전체 조회
     */
    List<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames);

    /**
     * 특정 게시판의 게시글을 페이징으로 조회
     */
    Page<PostEntity> findAllByBoardAndDeletedFalse(BoardEntity board, Pageable pageable);

    /**
     * 전체 게시판(자유/질문/리뷰)의 게시글을 페이징으로 조회
     */
    Page<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames, Pageable pageable);

    /**
     * 추천 수가 minLike 이상인 게시글을 페이징으로 조회 (삭제되지 않은 글만)
     * PostLikeEntity의 개수를 서브쿼리로 계산
     */
    @Query("""
        SELECT p
        FROM PostEntity p
        WHERE p.deleted = false
        AND (SELECT COUNT(l) FROM PostLikeEntity l WHERE l.post = p) >= :minLike
    """)
    Page<PostEntity> findPopularPosts(@Param("minLike") int minLike, Pageable pageable);
}