package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
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
        List<String> imageUrls = updated.getPostImageList().stream()
                .map(PostImageEntity::getPostImageUrl)
                .toList();
        return postConvertor.toDetailResponse(updated, imageUrls);
    }
}
