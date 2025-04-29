package aba3.lucid.user.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.dto.PostUpdateRequest;
import aba3.lucid.domain.board.entity.*;
import aba3.lucid.domain.board.enums.PostStatus;
import aba3.lucid.domain.board.repository.*;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
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
    private final PostImageRepository postImageRepository;
    private final BoardRepository boardRepository;
    private final UsersRepository usersRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;

    /**
     * 게시글 생성 서비스
     * - 사용자 및 게시판 존재 여부 검증
     * - 전체/인기 게시판 글 작성 금지
     * - 게시글 저장 (이미지는 content에 <img> 태그로 포함됨)
     */
    @Transactional
    public PostEntity createPost(String userId, String title, String content, Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        PostEntity post = PostEntity.builder()
                .postTitle(title)
                .postContent(content)
                .board(board)
                .usersEntity(user)
                .postStatus(PostStatus.CREATED)
                .postCreate(LocalDateTime.now())
                .postUpdate(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    /** 게시글 단건 조회 */
    @Transactional(readOnly = true)
    public PostEntity getPostById(long postId) {
        return postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));
    }

    /** 개별 게시판 글 목록 조회 */
    @Transactional(readOnly = true)
    public List<PostEntity> getPostListByBoardId(long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardAndDeletedFalse(board);
    }

    /** 전체 게시판(자유/질문/리뷰) 글 목록 */
    @Transactional(readOnly = true)
    public List<PostEntity> getAllCategoryPosts() {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames);
    }

    /** 게시글 수정 (이미지 로직 제거됨) */
    @Transactional
    public PostEntity updatePost(PostUpdateRequest request, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(request.getPostId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        post.updatePost(request.getPostTitle(), request.getPostContent(), LocalDateTime.now());
        return post;
    }

    /** 게시글 삭제 (논리 삭제) */
    @Transactional
    public void deletePost(long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        post.delete();
    }

    /** 게시글 추천 */
    @Transactional
    public void likePost(Long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        if (postLikeRepository.existsByPostPostIdAndUsersUserId(postId, userId)) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 추천한 게시글입니다.");
        }

        PostLikeEntity like = PostLikeEntity.builder()
                .post(post)
                .users(user)
                .build();
        postLikeRepository.save(like);
    }

    /** 게시글 조회수 증가 */
    @Transactional
    public void addPostView(Long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시글이 존재하지 않거나 삭제되었습니다."));

        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자가 존재하지 않습니다."));

        PostViewEntity view = PostViewEntity.builder()
                .post(post)
                .users(user)
                .build();
        postViewRepository.save(view);
    }

    /** 개별 게시판 페이징 */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPostPageByBoardId(Long boardId, Pageable pageable) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardAndDeletedFalse(board, pageable);
    }

    /** 전체 게시판 페이징 */
    @Transactional(readOnly = true)
    public Page<PostEntity> getAllCategoryPostPage(Pageable pageable) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findByBoardBoardNameInAndDeletedFalse(boardNames, pageable);
    }

    /** 인기 게시판 페이징 */
    @Transactional(readOnly = true)
    public Page<PostEntity> getPopularPostPage(Pageable pageable) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        return postRepository.findPopularPosts(boardNames, pageable);
    }

    /** 게시글 조회수 반환 */
    public long getViewCount(Long postId) {
        return postViewRepository.countByPostPostId(postId);
    }

    /** 게시글 추천 수 반환 */
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostPostId(postId);
    }
}
