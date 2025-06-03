// ✅ 리팩토링된 PostController.java
package aba3.lucid.board.controller;

import aba3.lucid.board.business.PostBusiness;
import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            summary = "게시글 작성",
            description = "게시글을 생성합니다. 게시판 ID와 제목, 내용을 입력받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "입력값 오류 혹은 전체/인기 게시판 작성 불가"),
                    @ApiResponse(responseCode = "404", description = "게시판 또는 사용자 없음")
            }
    )
    public API<PostDetailResponse> createPost(@RequestBody @Valid PostRequest request) {
        return API.OK(postBusiness.createPost(request));
    }

    /**
     * 게시글 상세 조회 API
     */
    @GetMapping("/{postId}")
    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 ID를 통해 상세 정보를 조회하고, 동시에 조회수를 증가시킵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "게시글 또는 사용자 없음")
            }
    )
    public API<PostDetailResponse> getPost(@PathVariable Long postId) {
        return API.OK(postBusiness.getPostById(postId));
    }

    /**
     * 게시글 수정 API
     */
    @PutMapping("")
    @Operation(
            summary = "게시글 수정",
            description = "게시글 제목과 내용을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공"),
                    @ApiResponse(responseCode = "403", description = "작성자 아님")
            }
    )
    public API<PostDetailResponse> updatePost(@RequestBody @Valid PostUpdateRequest request) {
        return API.OK(postBusiness.updatePost(request));
    }

    /**
     * 게시글 삭제 API
     */
    @DeleteMapping("/{postId}")
    @Operation(
            summary = "게시글 삭제",
            description = "게시글을 논리 삭제 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "작성자/관리자 아님")
            }
    )
    public API<String> deletePost(@PathVariable Long postId) {
        postBusiness.deletePost(postId);
        return API.OK("게시글이 삭제되었습니다.");
    }

    /**
     * 게시글 추천 API
     */
    @PostMapping("/{postId}/like")
    @Operation(
            summary = "게시글 추천",
            description = "게시글에 추천을 1회 등록합니다. 이미 추천했다면 에러 반환.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추천 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 추천함"),
            }
    )
    public API<String> likePost(@PathVariable Long postId) {
        postBusiness.likePost(postId);
        return API.OK("게시글을 추천했습니다.");
    }

    /**
     * 특정 게시판 게시글 목록 조회 (페이징)
     */
    @GetMapping("/list")
    @Operation(
            summary = "게시판 게시글 목록 조회",
            description = "게시판 ID를 기준으로 해당 게시판의 게시글 목록을 페이징으로 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")}
    )
    public API<PagedResponse<PostListResponse>> getPostPageByBoardId(
            @Parameter(description = "게시판 ID") @RequestParam Long boardId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        return API.OK(postBusiness.getPostPageByBoardId(boardId, page, size));
    }

    /**
     * 전체 게시판 게시글 목록 조회 (자유/질문/리뷰)
     */
    @GetMapping("/all")
    @Operation(
            summary = "전체 게시판 조회",
            description = "자유/질문/리뷰 게시판의 모든 게시글을 최신순으로 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")}
    )
    public API<PagedResponse<PostListResponse>> getAllCategoryPostPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        return API.OK(postBusiness.getAllCategoryPostPage(page, size));
    }

    /**
     * 인기 게시판 조회 (추천 수 15 이상)
     */
    @GetMapping("/popular")
    @Operation(
            summary = "인기 게시판 조회",
            description = "추천 수가 15 이상인 게시글을 최신순으로 페이징 조회합니다.",
            responses = {@ApiResponse(responseCode = "200", description = "조회 성공")}
    )
    public API<PagedResponse<PostListResponse>> getPopularPostPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size) {
        return API.OK(postBusiness.getPopularPostPage(page, size));
    }
} 
