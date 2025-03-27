package aba3.lucid.domain.user.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.user.business.CommentBusiness;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment Controller", description = "댓글/답글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentBusiness commentBusiness;

    /**
     * 댓글 또는 답글 작성 API
     *
     * - 댓글: parentCmtId 없이 요청
     * - 답글: parentCmtId 포함 요청
     */
    @PostMapping("")
    @Operation(
            summary = "댓글/답글 작성",
            description = "게시글에 댓글 또는 답글을 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "작성 성공"),
                    @ApiResponse(responseCode = "400", description = "대댓글 차수 제한 또는 유효성 실패"),
                    @ApiResponse(responseCode = "404", description = "게시글 또는 부모 댓글, 사용자 정보 없음")
            }
    )
    public API<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request,
            @RequestParam String userId // TODO: JWT 적용 전까지만 사용
    ) {
        CommentResponse response = commentBusiness.createComment(request, userId);
        return API.OK(response);
    }

    /**
     * 댓글/답글 목록 조회 API (계층 구조로 응답)
     *
     * - parentCmtId가 null인 댓글은 최상위 댓글
     * - 나머지는 해당 댓글의 자식으로 포함
     *
     * @param postId 댓글을 조회할 게시글 ID
     * @param userId 현재 사용자 ID (게시글 작성자 여부 판단용)
     * @return 계층 구조로 구성된 댓글 리스트
     */
    @GetMapping("/list")
    @Operation(
            summary = "댓글/답글 목록 조회",
            description = "게시글에 달린 댓글과 대댓글을 계층 구조로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공"),
                    @ApiResponse(responseCode = "404", description = "게시글이 존재하지 않음")
            }
    )
    public API<List<CommentResponse>> getCommentList(
            @RequestParam Long postId,
            @RequestParam String userId // TODO: JWT 적용 전까지만 사용
    ) {
        List<CommentResponse> comments = commentBusiness.getComments(postId, userId);
        return API.OK(comments);
    }
}