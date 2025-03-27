package aba3.lucid.domain.user.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.domain.user.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
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

    /**
     * 특정 게시글에 작성된 댓글/답글 목록 조회 (계층 구조)
     *
     * @param postId 대상 게시글 ID
     * @param userId 요청한 사용자 ID (작성자 여부 판단용)
     * @return 계층 구조로 구성된 댓글 응답 리스트
     */
    public List<CommentResponse> getComments(Long postId, String userId) {
        return commentService.getCommentsByPostId(postId, userId);
    }

    /**
     * 댓글 또는 대댓글 삭제 요청 처리
     *
     * - 댓글 작성자 본인 또는 운영자만 삭제 가능
     * - Soft Delete 방식으로 댓글 상태만 변경
     *
     * @param cmtId   삭제 대상 댓글 ID
     * @param userId  요청자(삭제 시도자) ID
     */
    public void deleteComment(Long cmtId, String userId) {
        commentService.deleteComment(cmtId, userId);
    }
}