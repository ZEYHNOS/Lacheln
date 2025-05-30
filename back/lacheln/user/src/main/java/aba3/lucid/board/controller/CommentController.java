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
     * - 사용자 인증 정보는 JWT에서 추출
     *
     * @param request 댓글 작성 요청 (postId, content 등)
     * @return 작성된 댓글 정보 응답
     */
    @PostMapping("")
    @Operation(
            summary = "댓글/답글 작성",
            description = "게시글에 댓글 또는 답글을 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "작성 성공"),
                    @ApiResponse(responseCode = "400", description = "차수 초과 또는 유효성 검증 실패"),
                    @ApiResponse(responseCode = "404", description = "게시글 또는 부모 댓글, 사용자 정보 없음")
            }
    )
    public API<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request
    ) {
        // JWT에서 인증된 사용자 ID 추출
        String userId = AuthUtil.getUserId();

        // 비즈니스 로직 호출 및 응답 반환
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
            @RequestParam Long postId
    ) {
        // 현재 인증된 사용자 ID 추출
        String userId = AuthUtil.getUserId();

        // 비즈니스 로직 호출
        List<CommentResponse> comments = commentBusiness.getComments(postId, userId);
        return API.OK(comments);
    }

    /**
     * 댓글 또는 대댓글 삭제 API (Soft Delete)
     *
     * - 댓글 작성자 본인 또는 운영자만 삭제 가능
     * - 실제 삭제가 아닌 상태만 DELETED로 변경
     *
     * @param cmtId 삭제할 댓글 ID
     * @return 삭제 결과 메시지
     */
    @DeleteMapping("/{cmtId}")
    @Operation(
            summary = "댓글/답글 삭제",
            description = "댓글 또는 대댓글을 삭제합니다. (Soft Delete 방식)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공"),
                    @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
                    @ApiResponse(responseCode = "404", description = "댓글 또는 사용자 정보 없음")
            }
    )
    public API<String> deleteComment(
            @PathVariable Long cmtId
    ) {
        // 현재 인증된 사용자 ID 추출
        String userId = AuthUtil.getUserId();

        // 삭제 비즈니스 로직 호출
        commentBusiness.deleteComment(cmtId, userId);
        return API.OK("댓글이 삭제되었습니다.");
    }
}
