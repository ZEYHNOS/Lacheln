package aba3.lucid.domain.board.repository;

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
     * 자유/질문/리뷰 게시판의 삭제되지 않은 게시글 전체 조회
     */
    List<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames);

    /**
     * 전체 게시판(자유/질문/리뷰)의 게시글을 페이징으로 조회
     */
    Page<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames, Pageable pageable);

    /**
     * 특정 게시판 ID 기준 게시글 페이징 조회 (삭제되지 않은 글만)
     * ✅ 기존: findAllByBoardAndDeletedFalse → ❌ 불안정
     * ✅ 수정: boardId 기준으로 명시적 조회
     */
    Page<PostEntity> findByBoardBoardIdAndDeletedFalse(Long boardId, Pageable pageable);

    /**
     * 전체 게시판(자유/질문/리뷰)의 게시글을 페이징 + 최신순(postCreate DESC)으로 조회
     */
    @Query("""
        SELECT p
        FROM PostEntity p
        WHERE p.board.boardName IN :boardNames
        AND p.deleted = false
        ORDER BY p.postCreate DESC
    """)
    Page<PostEntity> findAllCategoryPostsSortedByCreatedAt(
            @Param("boardNames") List<String> boardNames,
            Pageable pageable
    );

    /**
     * 인기 게시글 페이징 조회 (추천 수 15 이상 + 삭제되지 않은 글만)
     */
    @Query("""
        SELECT p
        FROM PostEntity p
        WHERE p.board.boardName IN :boardNames
        AND p.deleted = false
        AND SIZE(p.postLikeList) >= 15
        ORDER BY p.popularRegisteredAt DESC
    """)
    Page<PostEntity> findPopularPosts(
            @Param("boardNames") List<String> boardNames,
            Pageable pageable
    );
}
