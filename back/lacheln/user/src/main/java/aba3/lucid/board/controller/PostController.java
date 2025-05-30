package aba3.lucid.board.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.*;
import aba3.lucid.board.business.PostBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Post Controller", description = "게시글 관련 API")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostBusiness postBusiness;

    /**
     * 게시글 생성 API
     */
    @PostMapping("")
    @Operation(
            summary = "게시글 생성",
            description = "새로운 게시글을 생성합니다. (세미프로 이상부터 작성 가능)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "전체 & 인기 게시판 글 작성 불가"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판 또는 사용자")
            }
    )
    public API<PostDetailResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            @RequestParam String userId // TODO: JWT 구현 전까지만 사용
    ) {
        PostDetailResponse res = postBusiness.createPost(postRequest, userId);
        return API.OK(res);
    }

    /**
     * 게시글 단건 조회 API
     */
    @GetMapping("/{postId}")
    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글의 상세 내용을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않음")
            }
    )
    public API<PostDetailResponse> getPostById(
            @PathVariable Long postId,
            @RequestParam String userId // ← JWT 미구현 상태에서는 임시로 이렇게 받자!
    ) {
        PostDetailResponse res = postBusiness.getPostById(postId, userId);
        return API.OK(res);
    }

    /**
     * 인기 게시판 조회 API
     * - 자유/질문/리뷰 게시판 중 추천 수 15 이상 게시글만 조회 (페이징)
     */
    @GetMapping("/popular")
    @Operation(
            summary = "인기 게시판 조회",
            description = "자유/질문/리뷰 게시판 중 추천 15 이상인 인기 게시글들을 페이징으로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 게시글 목록 조회 성공")
            }
    )
    public API<PagedResponse<PostListResponse>> getPopularPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        PagedResponse<PostListResponse> response = postBusiness.getPopularPostPage(page, size);
        return API.OK(response);
    }

    /**
     * 게시글 수정 API
     * - 작성자 본인만 수정 가능 (임시로 userId 파라미터로 체크)
     * - 제목, 내용, 이미지 리스트를 수정할 수 있습니다.
     * - 기존 이미지는 모두 삭제 후, 새로운 이미지 리스트로 대체됩니다.
     * - 수정일(postUpdate)은 현재 시각으로 갱신됩니다.
     *
     * 예시 호출:
     * PUT /post/update?userId=test123
     * Body: {
     *   "postId": 1,
     *   "postTitle": "수정된 제목",
     *   "postContent": "수정된 본문",
     *   "imageUrls": ["https://img.com/a.jpg"]
     * }
     */
    @PutMapping("/update")
    @Operation(
            summary = "게시글 수정",
            description = "작성자가 본인의 게시글을 수정합니다. 기존 이미지 삭제도 가능합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "403", description = "작성자가 아님"),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
            }
    )
    public API<PostDetailResponse> updatePost(
            @RequestBody @Valid PostUpdateRequest request,
            @RequestParam String userId // TODO: JWT 적용 전 임시
    ) {
        PostDetailResponse response = postBusiness.updatePost(request, userId);
        return API.OK(response);
    }

    @DeleteMapping("/{postId}")
    @Operation(
            summary = "게시글 삭제",
            description = "게시글을 논리적으로 삭제합니다. 작성자 또는 관리자만 삭제할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
                    @ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않음")
            }
    )
    public API<Void> deletePost(
            @PathVariable Long postId,
            @RequestParam String userId // JWT 구현 전까지 임시 사용
    ) {
        postBusiness.deletePost(postId, userId);
        return API.OK((Void) null); // ★ 여기 수정!
    }

    /**
     * 게시글 추천 API
     * - 사용자가 게시글에 추천(좋아요)을 1회 누를 수 있습니다.
     * - 중복 추천은 불가능하며, 이미 추천한 경우 400 에러를 반환합니다.
     *
     * @param postId 추천할 게시글의 ID
     * @param userId 추천을 누른 사용자 ID (임시, JWT 적용 전)
     * @return 성공 시 200 OK 응답
     */
    @PostMapping("/{postId}/like")
    @Operation(
            summary = "게시글 추천",
            description = "게시글에 추천을 1회 할 수 있습니다. (중복 추천 불가)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추천 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 추천함"),
                    @ApiResponse(responseCode = "404", description = "게시글 또는 사용자 없음")
            }
    )
    public API<Void> likePost(
            @PathVariable Long postId,
            @RequestParam String userId // TODO: JWT 적용 후 삭제 예정
    ) {
        postBusiness.likePost(postId, userId);
        return API.OK((Void) null);
    }

    /**
     * 특정 게시판의 게시글을 페이징으로 조회
     *
     * 예시 호출:
     * GET /post/list?boardId=2&page=1&size=30
     *
     * @param boardId 게시판 ID
     * @param page 현재 페이지 번호 (기본 1)
     * @param size 페이지당 게시글 수 (기본 30)
     */
    @GetMapping("/list")
    @Operation(
            summary = "특정 게시판 페이징 조회",
            description = "특정 게시판에 작성된 게시글을 페이징으로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않음")
            }
    )
    public API<PagedResponse<PostListResponse>> getPostPageByBoardId(
            @RequestParam Long boardId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        PagedResponse<PostListResponse> response = postBusiness.getPostPageByBoardId(boardId, page, size);
        return API.OK(response);
    }

    /**
     * 전체 게시판 페이징 조회 (자유/질문/리뷰)
     *
     * 예시 호출:
     * GET /post/all?page=1&size=30
     *
     * @param page 현재 페이지 번호
     * @param size 한 페이지당 게시글 수
     */
    @GetMapping("/all")
    @Operation(
            summary = "전체 게시판 페이징 조회",
            description = "자유, 질문, 리뷰 게시판의 게시글을 통합해서 페이징 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공")
            }
    )
    public API<PagedResponse<PostListResponse>> getAllCategoryPostPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        PagedResponse<PostListResponse> response = postBusiness.getAllCategoryPostPage(page, size);
        return API.OK(response);
    }
}