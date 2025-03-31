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
     *
     * @param postId 게시글 ID
     * @return 삭제되지 않은 게시글(Optional)
     */
    Optional<PostEntity> findByPostIdAndDeletedFalse(Long postId);

    /**
     * 특정 게시판(BoardEntity)의 삭제되지 않은 게시글 전체 조회
     *
     * @param board 게시판 Entity
     * @return 삭제되지 않은 게시글 리스트
     */
    List<PostEntity> findAllByBoardAndDeletedFalse(BoardEntity board);

    /**
     * 자유/질문/리뷰 게시판의 삭제되지 않은 게시글 전체 조회
     *
     * @param boardNames 게시판 이름 리스트
     * @return 해당 게시판들의 게시글 리스트
     */
    List<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames);

    /**
     * 특정 게시판의 게시글을 페이징으로 조회 (삭제되지 않은 글만)
     *
     * @param board 게시판 Entity
     * @param pageable 페이징 정보 (정렬 포함 가능)
     * @return 게시글 Page 객체
     */
    Page<PostEntity> findAllByBoardAndDeletedFalse(BoardEntity board, Pageable pageable);

    /**
     * 전체 게시판(자유/질문/리뷰)의 게시글을 페이징으로 조회
     * - 기본 정렬 기준은 postCreate (작성일 기준 최신순)
     *
     * @param boardNames 전체 게시판 포함 리스트
     * @param pageable 페이징 정보
     * @return 게시글 Page 객체
     */
    Page<PostEntity> findByBoardBoardNameInAndDeletedFalse(List<String> boardNames, Pageable pageable);

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
     * - 인기 게시판 전용
     * - 인기 등록 시각(popularRegisteredAt) 기준으로 최신순 정렬
     *
     * @param boardNames 자유/질문/리뷰 게시판 이름 리스트
     * @param pageable 페이징 정보
     * @return 인기 게시글 Page 객체
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