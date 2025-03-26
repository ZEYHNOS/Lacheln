package aba3.lucid.domain.board.controller;

import aba3.lucid.common.api.API;
import aba3.lucid.domain.board.business.CommentBusiness;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}