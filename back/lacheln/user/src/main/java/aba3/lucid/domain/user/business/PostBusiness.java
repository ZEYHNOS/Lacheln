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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
     * 게시글 단건 조회 (조회수 증가 포함)
     * - 사용자가 게시글을 조회할 때마다 조회수를 1 증가시킴
     * - 게시글 + 이미지 목록 + 추천수 + 조회수 정보를 함께 반환
     *
     * @param postId 게시글 ID
     * @param userId 사용자 ID (조회자)
     * @return 상세 조회 응답 DTO
     */
    public PostDetailResponse getPostById(long postId, String userId) {
        // 1. 조회수 저장 (PostViewEntity에 기록)
        postService.addPostView(postId, userId);

        // 2. 게시글 엔티티 조회
        PostEntity post = postService.getPostById(postId);

        // 3. 이미지 URL 리스트 추출
        List<String> imageUrls = post.getPostImageList().stream()
                .map(PostImageEntity::getPostImageUrl)
                .toList();

        // 4. 추천수 및 조회수 조회
        int likeCount = (int) postService.getLikeCount(postId);
        int viewCount = (int) postService.getViewCount(postId);

        // 5. DTO 생성 및 반환
        return postConvertor.toDetailResponse(post, imageUrls, likeCount, viewCount);
    }

    /**
     * 특정 게시판에 속한 게시글 목록 조회
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
     * 자유/질문/리뷰 전체 게시판 목록 조회
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
     * 전체 게시판(자유/질문/리뷰)의 게시글을 페이징하여 조회하는 메서드
     *
     * 🔹 "전체 게시판"은 자유/질문/리뷰 게시판을 통합한 개념이며,
     * 🔹 이 메서드는 PageRequest를 통해 30개 단위로 페이지를 나누고,
     * 🔹 각 게시글의 추천 수, 조회 수까지 계산하여 응답에 포함시킨다.
     *
     * @param page 사용자가 요청한 페이지 번호 (1부터 시작)
     * @param size 페이지당 게시글 수 (기본값 30)
     * @return 페이징된 게시글 목록 + 페이징 정보 포함 응답
     */
    public PagedResponse<PostListResponse> getAllCategoryPostPage(int page, int size) {
        // 🔸 PageRequest.of()는 0부터 시작이므로 1페이지 → 0번 인덱스로 조정
        Pageable pageable = PageRequest.of(page - 1, size);

        // 🔸 자유/질문/리뷰 게시판에서 삭제되지 않은 게시글만 페이징 조회
        Page<PostEntity> result = postService.getAllCategoryPostPage(pageable);

        // 🔸 게시글 목록을 PostListResponse DTO로 변환
        List<PostListResponse> content = result.getContent().stream()
                .map(post -> {
                    int likeCount = (int) postService.getLikeCount(post.getPostId()); // 추천 수
                    int viewCount = (int) postService.getViewCount(post.getPostId()); // 조회 수
                    return postConvertor.toListResponse(post, likeCount, viewCount);
                }).toList();

        // 🔸 페이징 응답 객체 생성 및 반환
        return new PagedResponse<>(
                content,                      // 변환된 게시글 리스트
                result.getNumber() + 1,       // 0-index → 1-index
                result.getSize(),             // 페이지당 게시글 수
                result.getTotalElements(),    // 전체 게시글 수
                result.getTotalPages(),       // 전체 페이지 수
                result.hasNext(),             // 다음 페이지 존재 여부
                result.hasPrevious()          // 이전 페이지 존재 여부
        );
    }

    /**
     * 특정 게시판의 페이징된 게시글 목록을 응답으로 변환
     *
     * @param boardId 조회할 게시판 ID
     * @param page 현재 페이지 번호 (1부터 시작)
     * @param size 페이지당 게시글 수
     * @return 페이징된 게시글 응답
     */
    public PagedResponse<PostListResponse> getPostPageByBoardId(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // 스프링은 0부터 시작하므로 -1 처리
        Page<PostEntity> result = postService.getPostPageByBoardId(boardId, pageable);

        // 게시글 리스트 → PostListResponse 변환
        List<PostListResponse> content = result.getContent().stream()
                .map(post -> {
                    int likeCount = (int) postService.getLikeCount(post.getPostId());
                    int viewCount = (int) postService.getViewCount(post.getPostId());
                    return postConvertor.toListResponse(post, likeCount, viewCount);
                }).toList();

        // 최종 페이징 응답 DTO 반환
        return new PagedResponse<>(
                content,
                result.getNumber() + 1,  // 0-index → 1-index 변환
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    /**
     * 인기 게시판 페이징 조회 (추천 수 15 이상)
     *
     * @param page 현재 페이지 번호 (1부터 시작)
     * @param size 한 페이지당 게시글 수
     * @return 페이징된 인기 게시글 응답
     */
    public PagedResponse<PostListResponse> getPopularPostPage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // 페이지는 0부터 시작
        Page<PostEntity> result = postService.getPopularPostPage(15, pageable); // 추천 수 15 이상 기준

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
     * 게시글 추천 요청 처리
     * - 컨트롤러로부터 호출되어 Service를 통해 추천 로직 실행
     * - 추천 성공 시 아무 응답 없이 완료 처리
     *
     * @param postId 추천할 게시글 ID
     * @param userId 추천을 누른 사용자 ID
     */
    public void likePost(Long postId, String userId) {
        postService.likePost(postId, userId);
    }


    /**
     * 관리자 권한 여부 임시 체크
     * - 추후 회원 등급 기능이 구현되면 Role 기반으로 수정
     */
    private boolean isAdmin(String userId) {
        return userId.equals("admin123"); // ★ 임시 관리자 계정 지정
    }

}
