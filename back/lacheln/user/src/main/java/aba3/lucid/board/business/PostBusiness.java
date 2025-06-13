package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.PostConvertor;
import aba3.lucid.domain.board.dto.*;
import aba3.lucid.domain.board.entity.BoardEntity;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.TierEnum;
import aba3.lucid.board.service.PostService;
import aba3.lucid.user.service.UserService;
import aba3.lucid.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class PostBusiness {

    private final PostService postService;
    private final UserService userService;
    private final BoardRepository boardRepository;
    private final PostConvertor postConvertor;

    /**
     * 게시글 생성
     * - 세미프로 이상만 가능
     * - 전체/인기 게시판은 작성 불가
     */
    public PostDetailResponse createPost(PostRequest request) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        if (user.getUserTier().ordinal() < TierEnum.SEMI_PRO.ordinal()) {
            throw new ApiException(ErrorCode.FORBIDDEN, "세미프로 이상만 게시글을 작성할 수 있습니다.");
        }

        BoardEntity board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        PostEntity entity = postConvertor.toEntity(request, board, user);
        PostEntity saved = postService.savePost(entity);
        return postConvertor.toDetailResponse(saved);
    }

    /**
     * 게시글 상세 조회
     * - 비회원도 조회 가능
     * - 조회수는 로그인한 사용자만 기록
     */
    public PostDetailResponse getPostById(Long postId) {
        PostEntity post = postService.getPostById(postId);

        boolean hasLiked = false;

        // 로그인 사용자 여부 체크
        try {
            String userId = AuthUtil.getUserId();
            UsersEntity user = userService.findByIdWithThrow(userId);
            postService.addPostView(post, user); // 조회수 기록
            hasLiked = postService.hasLiked(postId, userId);
        } catch (Exception e) {
            // 비회원이거나 인증 실패 → 조회수 기록 생략
        }

        int likeCount = (int) postService.getLikeCount(postId);
        int viewCount = (int) postService.getViewCount(postId);
        return postConvertor.toDetailResponse(post, likeCount, viewCount, hasLiked);
    }

    /**
     * 게시글 수정
     * - 작성자만 가능
     */
    public PostDetailResponse updatePost(PostUpdateRequest request) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        PostEntity post = postService.getPostById(request.getPostId());

        if (!post.getUsersEntity().getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        PostEntity updated = postService.updatePost(post, request);
        int likeCount = (int) postService.getLikeCount(post.getPostId());
        int viewCount = (int) postService.getViewCount(post.getPostId());
        return postConvertor.toDetailResponse(updated, likeCount, viewCount);
    }

    /**
     * 게시글 삭제
     * - 작성자 또는 관리자만 가능
     */
    public void deletePost(Long postId) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        PostEntity post = postService.getPostById(postId);
        boolean isWriter = post.getUsersEntity().getUserId().equals(userId);
        boolean isAdmin = isAdmin(userId);

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        postService.deletePost(post);
    }

    /**
     * 게시글 추천
     * - 세미프로 이상만 가능
     * - 중복 추천 불가
     */
    public void likePost(Long postId) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        if (user.getUserTier().ordinal() < TierEnum.SEMI_PRO.ordinal()) {
            throw new ApiException(ErrorCode.FORBIDDEN, "세미프로 이상만 추천할 수 있습니다.");
        }

        PostEntity post = postService.getPostById(postId);
        postService.likePost(post, user);
    }

    /**
     * 특정 게시판의 게시글 목록 조회 (페이징)
     */
    public PagedResponse<PostListResponse> getPostPageByBoardId(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getPostPageByBoardId(boardId, pageable);
        return toPagedResponse(result);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 목록 조회 (페이징)
     */
    public PagedResponse<PostListResponse> getAllCategoryPostPage(int page, int size) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판", "핫딜게시판");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getAllCategoryPostPage(boardNames, pageable);
        return toPagedResponse(result);
    }

    /**
     * 인기 게시판(추천 수 15 이상) 목록 조회 (페이징)
     */
    public PagedResponse<PostListResponse> getPopularPostPage(int page, int size) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판", "핫딜게시판");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PostEntity> result = postService.getPopularPostPage(boardNames, pageable);
        return toPagedResponse(result);
    }

    /**
     * Page<PostEntity> → PagedResponse<PostListResponse> 변환
     */
    private PagedResponse<PostListResponse> toPagedResponse(Page<PostEntity> result) {
        List<PostListResponse> content = result.getContent().stream()
                .map(post -> {
                    int likeCount = (int) postService.getLikeCount(post.getPostId());
                    int viewCount = (int) postService.getViewCount(post.getPostId());
                    return postConvertor.toListResponse(post, likeCount, viewCount);
                }).toList();

        return new PagedResponse<>(
                content,
                result.getNumber() + 1,
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    /**
     * 관리자 권한 확인
     */
    private boolean isAdmin(String userId) {
        UsersEntity user = userService.findByIdWithThrow(userId);
        return user.getUserTier() == TierEnum.ADMIN;
    }

    public PagedResponse<PostListResponse> searchPostList(Long boardId, int page, int size, String type, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PostEntity> result;

        if (boardId != null) {
            result = postService.searchByBoardId(boardId, type, keyword, pageable);
        } else {
            result = postService.searchAllBoards(type, keyword, pageable);
        }

        return toPagedResponse(result);
    }
}
