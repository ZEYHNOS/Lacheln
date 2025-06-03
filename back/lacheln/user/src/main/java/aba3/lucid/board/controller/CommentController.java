package aba3.lucid.board.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.board.business.CommentBusiness;
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
     * - 일반 댓글: parentCmtId 없이 요청
     * - 대댓글: parentCmtId 포함 요청
     * - 세미프로 이상만 작성 가능
     */
    @PostMapping("")
    @Operation(
            summary = "댓글/답글 작성",
            description = "세미프로 등급 이상만 댓글 또는 답글을 작성할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "작성 성공"),
                    @ApiResponse(responseCode = "400", description = "차수 초과 또는 유효성 검증 실패"),
                    @ApiResponse(responseCode = "403", description = "세미프로 미만 등급"),
                    @ApiResponse(responseCode = "404", description = "게시글 또는 부모 댓글, 사용자 정보 없음")
            }
    )
    public API<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
        String userId = AuthUtil.getUserId();
        CommentResponse response = commentBusiness.createComment(request, userId);
        return API.OK(response);
    }

    /**
     * 댓글/답글 목록 조회 API
     *
     * - 계층 구조로 구성된 댓글 반환
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
    public API<List<CommentResponse>> getCommentList(@RequestParam Long postId) {
        String userId = AuthUtil.getUserId();
        List<CommentResponse> comments = commentBusiness.getComments(postId, userId);
        return API.OK(comments);
    }

    /**
     * 댓글 또는 대댓글 삭제 API
     *
     * - 댓글 작성자 또는 ADMIN만 삭제 가능
     * - 삭제 시 자식 댓글까지 함께 삭제
     */
    @DeleteMapping("/{cmtId}")
    @Operation(
            summary = "댓글/답글 삭제",
            description = "댓글 또는 대댓글을 삭제합니다. 작성자 또는 관리자만 가능하며, 자식 댓글도 함께 삭제됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
                    @ApiResponse(responseCode = "404", description = "댓글 또는 사용자 정보 없음")
            }
    )
    public API<String> deleteComment(@PathVariable Long cmtId) {
        String userId = AuthUtil.getUserId();
        commentBusiness.deleteComment(cmtId, userId);
        return API.OK("댓글이 삭제되었습니다.");
    }
}