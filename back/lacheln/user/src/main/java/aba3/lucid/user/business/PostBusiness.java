package aba3.lucid.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.entity.PostImageEntity;
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
     * 게시글 생성 처리
     * - 사용자, 게시판 검증 → 게시글 저장 → 응답 DTO 변환
     *
     * @param postRequest 게시글 작성 요청 DTO
     * @param userId      작성자 ID
     * @return 상세 응답 DTO
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
     * 게시글 단건 상세 조회 + 조회수 증가
     * - 1. 조회수 증가 (PostViewEntity 저장)
     * - 2. 게시글 조회 (삭제 여부 확인 포함)
     * - 3. 이미지 URL, 추천수, 조회수 조회
     * - 4. DTO 변환 및 반환
     */
    public PostDetailResponse getPostById(long postId, String userId) {
        postService.addPostView(postId, userId); // 조회수 저장
        PostEntity post = postService.getPostById(postId); // 게시글 조회

        List<String> imageUrls = post.getPostImageList().stream()
                .map(PostImageEntity::getPostImageUrl)
                .toList();

        int likeCount = (int) postService.getLikeCount(postId);
        int viewCount = (int) postService.getViewCount(postId);

        return postConvertor.toDetailResponse(post, imageUrls, likeCount, viewCount);
    }

    /**
     * 특정 게시판의 전체 게시글 목록 조회 (비페이징)
     *
     * @param boardId 게시판 ID
     * @return 게시글 목록 (추천/조회수 포함)
     */
    public List<PostListResponse> getPostListByBoardId(long boardId) {
        List<PostEntity> posts = postService.getPostListByBoardId(boardId);

        return posts.stream()
                .map(post -> {
                    int likeCount = (int) postService.getLikeCount(post.getPostId());
                    int viewCount = (int) postService.getViewCount(post.getPostId());
                    return postConvertor.toListResponse(post, likeCount, viewCount);
                })
                .toList();
    }

    /**
     * 자유/질문/리뷰 게시판 전체 목록 조회 (비페이징)
     * - 전체 게시판은 자유/질문/리뷰를 통합한 개념
     */
    public List<PostListResponse> getAllCategoryPosts() {
        List<PostEntity> posts = postService.getAllCategoryPosts();

        return posts.stream()
                .map(post -> {
                    int likeCount = (int) postService.getLikeCount(post.getPostId());
                    int viewCount = (int) postService.getViewCount(post.getPostId());
                    return postConvertor.toListResponse(post, likeCount, viewCount);
                })
                .toList();
    }

    /**
     * 게시글 수정 처리
     * - 제목, 내용, 이미지 수정 포함
     * - 작성자 본인 확인
     *
     * @param request 수정 요청 DTO
     * @param userId  요청자 ID
     * @return 수정된 게시글 상세 응답
     */
    public PostDetailResponse updatePost(PostUpdateRequest request, String userId) {
        PostEntity updated = postService.updatePost(request, userId);

        List<String> imageUrls = updated.getPostImageList().stream()
                .map(PostImageEntity::getPostImageUrl)
                .toList();

        return postConvertor.toDetailResponse(updated, imageUrls);
    }

    /**
     * 게시글 삭제 요청 처리
     * - 작성자 또는 관리자만 가능
     *
     * @param postId 게시글 ID
     * @param userId 요청자 ID
     */
    public void deletePost(long postId, String userId) {
        PostEntity post = postService.getPostById(postId);

        boolean isWriter = post.getUsersEntity().getUserId().equals(userId);
        boolean isAdmin = isAdmin(userId); // 추후 등급 기반으로 대체 예정

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        postService.deletePost(postId, userId);
    }

    /**
     * 전체 게시판(자유/질문/리뷰) 페이징 조회
     * - 정렬 기준: postCreate DESC (최신순)
     * - 추천 수 / 조회 수 포함하여 PostListResponse로 반환
     *
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지당 게시글 수
     * @return 페이징 응답 DTO
     */
    public PagedResponse<PostListResponse> getAllCategoryPostPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending()); // PageRequest는 0부터 시작
        Page<PostEntity> result = postService.getAllCategoryPostPage(pageable);

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
     * 특정 게시판의 페이징된 게시글 목록 조회
     * - 정렬 기준: postCreate DESC (최신순)
     *
     * @param boardId 게시판 ID
     * @param page 페이지 번호 (1부터 시작)
     * @param size 한 페이지 게시글 수
     * @return 페이징 응답 DTO
     */
    public PagedResponse<PostListResponse> getPostPageByBoardId(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("postCreate").descending());
        Page<PostEntity> result = postService.getPostPageByBoardId(boardId, pageable);

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
     * 인기 게시판 페이징 조회
     * - 조건: 추천 수 15 이상
     * - 정렬 기준: popularRegisteredAt DESC (인기 등록 시점 기준 최신순)
     *
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기
     * @return 인기 게시글 페이징 응답
     */
    public PagedResponse<PostListResponse> getPopularPostPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PostEntity> result = postService.getPopularPostPage(pageable); // 내부에서 추천 수 15 이상 조건 포함

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
     * 게시글 추천 처리
     * - 중복 추천 방지 포함
     *
     * @param postId 추천할 게시글 ID
     * @param userId 추천자 ID
     */
    public void likePost(Long postId, String userId) {
        postService.likePost(postId, userId);
    }

    /**
     * 관리자 여부 확인 (임시 구현)
     * - 추후 회원 등급/Role 기반 권한 시스템으로 대체 예정
     *
     * @param userId 사용자 ID
     * @return 관리자 여부
     */
    private boolean isAdmin(String userId) {
        return userId.equals("admin123"); // TODO: 나중에 Role enum으로 교체 예정
    }
}