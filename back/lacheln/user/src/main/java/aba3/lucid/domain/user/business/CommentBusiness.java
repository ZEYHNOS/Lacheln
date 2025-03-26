package aba3.lucid.domain.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.domain.board.service.CommentService;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class CommentBusiness {

    private final CommentService commentService;
    private final CommentConvertor commentConvertor;

    /**
     * 댓글 작성 처리 (댓글 or 대댓글)
     *
     * @param request 댓글 작성 요청
     * @param userId  작성자 ID
     * @return 응답 DTO
     */
    public CommentResponse createComment(CommentRequest request, String userId) {
        CommentEntity comment = commentService.saveComment(
                request.getPostId(),
                request.getParentCmtId(),
                request.getCmtContent(),
                userId
        );

        // 게시글 작성자인지 여부 확인 (댓글 작성자 vs 게시글 작성자 비교)
        boolean isPostWriter = comment.getPost().getUsersEntity().getUserId().equals(userId);

        return commentConvertor.toResponse(comment, isPostWriter);
    }
}