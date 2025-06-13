// ✅ 리팩토링된 CommentController.java
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
@RequestMapping("/board/comment")
public class CommentController {

    private final CommentBusiness commentBusiness;

    /**
     * ✅ 댓글 또는 답글 작성 API
     *
     * - 일반 댓글: parentCmtId 없이 요청
     * - 대댓글: parentCmtId 포함 요청
     * - 세미프로 이상만 작성 가능 (userTier로 판단)
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
        // 🔐 인증 정보에서 userId 추출 (세미프로 이상만 작성 가능)
        String userId = AuthUtil.getUserId();
        CommentResponse response = commentBusiness.createComment(request, userId);
        return API.OK(response);
    }

    /**
     * ✅ 댓글/답글 목록 조회 API (비회원도 가능)
     *
     * - 인증 여부와 관계없이 게시글에 달린 댓글/답글을 계층 구조로 모두 조회합니다.
     * - 게시글 상세 페이지 진입 시 자동 호출됨
     * - 작성자 여부, 대댓글 포함 여부는 내부에서 처리
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
        String userId = null;

        try {
            // ✅ 비회원일 경우 예외가 발생하므로 try-catch로 감싸서 null로 처리
            userId = AuthUtil.getUserId();
        } catch (Exception e) {
            // 비회원 또는 인증 객체 형식 오류 → null로 간주하여 전체 댓글 조회 허용
            userId = null;
        }

        // ✅ 비회원이어도 userId == null로 조회 가능하도록 설계됨
        List<CommentResponse> comments = commentBusiness.getComments(postId, userId);
        return API.OK(comments);
    }

    /**
     * ✅ 댓글 또는 대댓글 삭제 API
     *
     * - 작성자 본인 또는 ADMIN만 삭제 가능
     * - 자식 댓글(대댓글)도 함께 Soft Delete 처리됨
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
        // 🔐 인증된 사용자만 삭제 가능
        String userId = AuthUtil.getUserId();
        commentBusiness.deleteComment(cmtId, userId);
        return API.OK("댓글이 삭제되었습니다.");
    }
}
