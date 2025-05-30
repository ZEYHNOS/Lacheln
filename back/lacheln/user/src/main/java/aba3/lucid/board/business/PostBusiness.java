// ✅ 리팩토링된 PostBusiness.java (Business 계층 - 흐름 제어 & 검증 담당)
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
     * 게시글 생성 처리
     */
    public PostDetailResponse createPost(PostRequest request) {
        // 유저 인증 정보 가져오기
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        // 게시판 존재 여부 확인
        BoardEntity board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        // 전체/인기 게시판에는 작성 불가
        if (board.getBoardName().equals("전체") || board.getBoardName().equals("인기")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "전체/인기 게시판에는 글을 작성할 수 없습니다.");
        }

        // Convertor로 Entity 변환 후 저장
        PostEntity entity = postConvertor.toEntity(request, board, user);
        PostEntity saved = postService.savePost(entity);

        return postConvertor.toDetailResponse(saved);
    }

    /**
     * 게시글 단건 상세 조회 + 조회수 증가
     */
    public PostDetailResponse getPostById(Long postId) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);

        PostEntity post = postService.getPostById(postId);
        postService.addPostView(post, user);

        int likeCount = (int) postService.getLikeCount(postId);
        int viewCount = (int) postService.getViewCount(postId);

        return postConvertor.toDetailResponse(post, likeCount, viewCount);
    }

    /**
     * 게시글 수정 처리
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
     * 게시글 삭제 처리
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
     * 게시글 추천 처리
     */
    public void likePost(Long postId) {
        String userId = AuthUtil.getUserId();
        UsersEntity user = userService.findByIdWithThrow(userId);
        PostEntity post = postService.getPostById(postId);
        postService.likePost(post, user);
    }

    /**
     * 특정 게시판 페이징 조회
     */
    public PagedResponse<PostListResponse> getPostPageByBoardId(Long boardId, int page, int size) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "게시판이 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getPostPageByBoardId(board, pageable);

        return toPagedResponse(result);
    }

    /**
     * 전체 게시판 페이징 조회 (자유/질문/리뷰)
     */
    public PagedResponse<PostListResponse> getAllCategoryPostPage(int page, int size) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getAllCategoryPostPage(boardNames, pageable);

        return toPagedResponse(result);
    }

    /**
     * 인기 게시판 조회 (추천 수 15 이상)
     */
    public PagedResponse<PostListResponse> getPopularPostPage(int page, int size) {
        List<String> boardNames = List.of("자유게시판", "질문게시판", "리뷰게시판");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PostEntity> result = postService.getPopularPostPage(boardNames, pageable);

        return toPagedResponse(result);
    }

    /**
     * 페이징 결과를 PagedResponse로 변환
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
     * 관리자 권한 확인 (임시)
     */
    private boolean isAdmin(String userId) {
        return userId.equals("admin123"); // TODO: 추후 Role 기반 권한으로 대체 예정
    }
}