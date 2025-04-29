package aba3.lucid.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.PostConvertor;
import aba3.lucid.domain.board.dto.*;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.user.service.PostService;
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
    private final PostConvertor postConvertor;

    /**
     * 게시글 생성
     * - postContent 안에 <img> 태그 삽입 가능
     */
    public PostDetailResponse createPost(PostRequest postRequest, String userId) {
        PostEntity post = postService.createPost(
                userId,
                postRequest.getPostTitle(),
                postRequest.getPostContent(),
                postRequest.getBoardId()
        );
        return postConvertor.toDetailResponse(post, 0, 0);
    }

    /**
     * 게시글 단건 조회 + 조회수 증가
     */
    public PostDetailResponse getPostById(long postId, String userId) {
        postService.addPostView(postId, userId);
        PostEntity post = postService.getPostById(postId);

        int likeCount = (int) postService.getLikeCount(postId);
        int viewCount = (int) postService.getViewCount(postId);

        return postConvertor.toDetailResponse(post, likeCount, viewCount);
    }

    /**
     * 특정 게시판 게시글 목록 조회 (비페이징)
     */
    public List<PostListResponse> getPostListByBoardId(long boardId) {
        List<PostEntity> posts = postService.getPostListByBoardId(boardId);
        return posts.stream()
                .map(post -> postConvertor.toListResponse(post,
                        (int) postService.getLikeCount(post.getPostId()),
                        (int) postService.getViewCount(post.getPostId())))
                .toList();
    }

    /**
     * 자유/질문/리뷰 통합 목록 조회 (비페이징)
     */
    public List<PostListResponse> getAllCategoryPosts() {
        List<PostEntity> posts = postService.getAllCategoryPosts();
        return posts.stream()
                .map(post -> postConvertor.toListResponse(post,
                        (int) postService.getLikeCount(post.getPostId()),
                        (int) postService.getViewCount(post.getPostId())))
                .toList();
    }

    /**
     * 게시글 수정
     * - 제목, 본문 수정
     */
    public PostDetailResponse updatePost(PostUpdateRequest request, String userId) {
        PostEntity updated = postService.updatePost(request, userId);
        return postConvertor.toDetailResponse(updated, 0, 0);
    }

    /**
     * 게시글 삭제
     * - 작성자 또는 관리자만 가능
     */
    public void deletePost(long postId, String userId) {
        PostEntity post = postService.getPostById(postId);
        boolean isWriter = post.getUsersEntity().getUserId().equals(userId);
        boolean isAdmin = isAdmin(userId);

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        postService.deletePost(postId, userId);
    }

    /**
     * 전체 게시판 페이징 조회 (최신순)
     */
    public PagedResponse<PostListResponse> getAllCategoryPostPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getAllCategoryPostPage(pageable);

        List<PostListResponse> content = result.getContent().stream()
                .map(post -> postConvertor.toListResponse(post,
                        (int) postService.getLikeCount(post.getPostId()),
                        (int) postService.getViewCount(post.getPostId())))
                .toList();

        return new PagedResponse<>(content, result.getNumber() + 1, result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext(), result.hasPrevious());
    }

    /**
     * 특정 게시판 페이징 조회
     */
    public PagedResponse<PostListResponse> getPostPageByBoardId(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getPostPageByBoardId(boardId, pageable);

        List<PostListResponse> content = result.getContent().stream()
                .map(post -> postConvertor.toListResponse(post,
                        (int) postService.getLikeCount(post.getPostId()),
                        (int) postService.getViewCount(post.getPostId())))
                .toList();

        return new PagedResponse<>(content, result.getNumber() + 1, result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext(), result.hasPrevious());
    }

    /**
     * 인기 게시판 페이징 조회 (추천수 15 이상)
     */
    public PagedResponse<PostListResponse> getPopularPostPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PostEntity> result = postService.getPopularPostPage(pageable);

        List<PostListResponse> content = result.getContent().stream()
                .map(post -> postConvertor.toListResponse(post,
                        (int) postService.getLikeCount(post.getPostId()),
                        (int) postService.getViewCount(post.getPostId())))
                .toList();

        return new PagedResponse<>(content, result.getNumber() + 1, result.getSize(),
                result.getTotalElements(), result.getTotalPages(), result.hasNext(), result.hasPrevious());
    }

    /**
     * 게시글 추천 (중복 추천 방지)
     */
    public void likePost(Long postId, String userId) {
        postService.likePost(postId, userId);
    }

    /**
     * 관리자 여부 확인 (임시)
     */
    private boolean isAdmin(String userId) {
        return userId.equals("admin123");
    }
}