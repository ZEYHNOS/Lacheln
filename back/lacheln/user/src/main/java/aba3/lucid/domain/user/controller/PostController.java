package aba3.lucid.domain.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.*;
import aba3.lucid.domain.user.business.PostBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public API<PostDetailResponse> getPostById(@PathVariable long postId) {
        PostDetailResponse res = postBusiness.getPostById(postId);
        return API.OK(res);
    }

    /**
     * 특정 게시판의 게시글 목록 조회 API
     */
    @GetMapping("/list")
    @Operation(
            summary = "특정 게시판 목록 조회",
            description = "특정 게시판에 작성된 게시글 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 게시판이 존재하지 않음")
            }
    )
    public API<List<PostListResponse>> getPostListByBoardId(@RequestParam long boardId) {
        List<PostListResponse> responseList = postBusiness.getPostListByBoardId(boardId);
        return API.OK(responseList);
    }

    /**
     * 전체 게시판 통합 목록 조회 API
     */
    @GetMapping("/all")
    @Operation(
            summary = "전체 게시판 조회",
            description = "자유/질문/리뷰 게시판의 모든 글을 통합 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "전체 게시판 목록 조회 성공")
            }
    )
    public API<List<PostListResponse>> getAllCategoryPosts() {
        return API.OK(postBusiness.getAllCategoryPosts());
    }

    /**
     * 인기 게시판 조회 API
     * - 자유/질문/리뷰 게시판 중 추천 수 15 이상 게시글만 조회
     */
    @GetMapping("/popular")
    @Operation(
            summary = "인기 게시판 조회",
            description = "자유/질문/리뷰 게시판 중 추천 15 이상인 인기 게시글들을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "인기 게시글 목록 조회 성공")
            }
    )
    public ResponseEntity<List<PostListResponse>> getPopularPosts() {
        List<PostListResponse> response = postBusiness.getPopularPostList();
        return ResponseEntity.ok(response);
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
            @PathVariable long postId,
            @RequestParam String userId // JWT 구현 전까지 임시 사용
    ) {
        postBusiness.deletePost(postId, userId);
        return API.OK((Void) null); // ★ 여기 수정!
    }
}
