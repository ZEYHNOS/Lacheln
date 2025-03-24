package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.entity.PostImageEntity;
import aba3.lucid.domain.board.convertor.PostConvertor;
import aba3.lucid.domain.board.dto.*;
import aba3.lucid.domain.board.entity.PostEntity;
import aba3.lucid.domain.user.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class PostBusiness {

    private final PostService postService;
    private final PostConvertor postConvertor;

    /**
     * 게시글 생성 (Request → Entity → Response)
     */
    public PostDetailResponse createPost(PostRequest postRequest, String userId) {
        PostEntity post = postService.createPost(
                userId,
                postRequest.getPostTitle(),
                postRequest.getPostContent(),
                postRequest.getBoardId(),
                postRequest.getImageUrls()
        );
        return postConvertor.toDetailResponse(post, postRequest.getImageUrls());
    }

    /**
     * 게시글 단건 조회
     */
    public PostDetailResponse getPostById(long postId) {
        PostEntity post = postService.getPostById(postId);
        List<String> imageUrls = post.getPostImageList().stream()
                .map(img -> img.getPostImageUrl())
                .toList();
        return postConvertor.toDetailResponse(post, imageUrls);
    }

    /**
     * 특정 게시판에 속한 게시글 목록 조회
     */
    public List<PostListResponse> getPostListByBoardId(long boardId) {
        List<PostEntity> posts = postService.getPostListByBoardId(boardId);
        return posts.stream()
                .map(post -> postConvertor.toListResponse(post, 0, 0)) // 추천/조회수 추후 추가
                .toList();
    }

    /**
     * 자유/질문/리뷰 전체 게시판 목록 조회
     */
    public List<PostListResponse> getAllCategoryPosts() {
        List<PostEntity> posts = postService.getAllCategoryPosts();
        return posts.stream()
                .map(post -> postConvertor.toListResponse(post, 0, 0))
                .toList();
    }

    /**
     * 인기 게시판 조회 (추천수 15 이상)
     */
    public List<PostListResponse> getPopularPostList() {
        List<PostEntity> posts = postService.getPopularPosts();
        return posts.stream()
                .map(post -> postConvertor.toListResponse(post, 0, 0))
                .toList();
    }

    /**
     * 게시글 수정
     */
    public PostDetailResponse updatePost(PostUpdateRequest request, String userId) {
        PostEntity updated = postService.updatePost(request, userId);

        // 최종 이미지 URL 목록 추출
        List<String> imageUrls = updated.getPostImageList().stream()
                .map(PostImageEntity::getPostImageUrl)
                .toList();

        return postConvertor.toDetailResponse(updated, imageUrls);
    }

    /**
     * 게시글 삭제 요청 처리 (작성자 본인만 삭제 가능)
     */
    public void deletePost(long postId, String userId) {
        PostEntity post = postService.getPostById(postId);

        // 작성자 또는 관리자 권한 확인
        boolean isWriter = post.getUsersEntity().getUserId().equals(userId);
        boolean isAdmin = isAdmin(userId); // ★ 임시 관리자 판별

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        postService.deletePost(postId, userId);
    }

    /**
     * 관리자 권한 여부 임시 체크
     * - 추후 회원 등급 기능이 구현되면 Role 기반으로 수정
     */
    private boolean isAdmin(String userId) {
        return userId.equals("admin123"); // ★ 임시 관리자 계정 지정
    }

}
