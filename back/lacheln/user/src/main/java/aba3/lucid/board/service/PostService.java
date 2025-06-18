package aba3.lucid.board.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.dto.PostUpdateRequest;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.board.entity.PostLikeEntity;
import aba3.lucid.domain.board.entity.PostViewEntity;
import aba3.lucid.domain.board.repository.BoardRepository;
import aba3.lucid.domain.board.repository.PostLikeRepository;
import aba3.lucid.domain.board.repository.PostRepository;
import aba3.lucid.domain.board.repository.PostViewRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;
    private final BoardRepository boardRepository;

    /**
     * 게시글 저장
     */
    @Transactional
    public PostEntity savePost(PostEntity post) {
        return postRepository.save(post);
    }

    /**
     * 게시글 단건 조회 (삭제되지 않은 글만)
     */
    @Transactional(readOnly = true)
    public PostEntity getPostById(long postId) {
        return postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));
    }

    /**
     * 게시글 삭제 처리 (논리 삭제)
     */
    @Transactional
    public void deletePost(PostEntity post) {
        post.delete();
    }

    /**
     * 게시글 수정 처리 (내용/제목 수정)
     */
    @Transactional
    public PostEntity updatePost(PostEntity post, PostUpdateRequest request) {
        post.updatePost(request.getPostTitle(), request.getPostContent(), LocalDateTime.now());

        // ✅ boardId가 변경됐을 경우에만 새로운 게시판 엔티티로 갱신
        if (!post.getBoard().getBoardId().equals(request.getBoardId())) {
            BoardEntity newBoard = boardRepository.findById(request.getBoardId())
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "변경할 게시판이 존재하지 않습니다."));
            post.setBoard(newBoard);
        }

        return post;
    }

    /**
     * ✅ 개별 게시판의 게시글 페이징 조회 (boardId 기반으로 수정됨)
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPostPageByBoardId(Long boardId, Pageable pageable) {
        return postRepository.findByBoardBoardIdAndDeletedFalse(boardId, pageable);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 페이징 조회
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getAllCategoryPostPage(List<String> boardNames, Pageable pageable) {
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames, pageable);
    }

    /**
     * 인기 게시판 페이징 조회 (추천 수 15 이상)
     */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPopularPostPage(List<String> boardNames, Pageable pageable) {
        return postRepository.findPopularPosts(boardNames, pageable);
    }

    /**
     * 게시글 추천 저장
     */
    @Transactional
    public void likePost(PostEntity post, UsersEntity user) {
        if (postLikeRepository.existsByPostPostIdAndUsersUserId(post.getPostId(), user.getUserId())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 추천한 게시글입니다.");
        }

        PostLikeEntity like = PostLikeEntity.builder()
                .post(post)
                .users(user)
                .build();
        postLikeRepository.save(like);
    }

    /**
     * 게시글 조회 저장
     */
    @Transactional
    public void addPostView(PostEntity post, UsersEntity user) {
        PostViewEntity view = PostViewEntity.builder()
                .post(post)
                .users(user)
                .build();
        postViewRepository.save(view);
    }

    /**
     * 게시글 추천 수 조회
     */
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostPostId(postId);
    }

    /**
     * 게시글 조회 수 조회
     */
    public long getViewCount(Long postId) {
        return postViewRepository.countByPostPostId(postId);
    }

    // 게시글 추천 여부 확인
    @Transactional(readOnly = true)
    public boolean hasLiked(Long postId, String userId) {
        return postLikeRepository.existsByPostPostIdAndUsersUserId(postId, userId);
    }

    @Transactional(readOnly = true)
    public Page<PostEntity> searchByBoardId(Long boardId, String type, String keyword, Pageable pageable) {
        return postRepository.searchByBoardId(boardId, type, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PostEntity> searchAllBoards(String type, String keyword, Pageable pageable) {
        return postRepository.searchAllBoards(type, keyword, pageable);
    }
}
