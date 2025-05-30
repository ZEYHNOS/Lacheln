package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.auth.AuthUtil;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.board.service.CommentService;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class CommentBusiness {

    private final CommentService commentService;
    private final CommentConvertor commentConvertor;
    private final UserService userService; // 인증된 사용자 조회용

    /**
     * 댓글 또는 대댓글 작성 요청 처리
     *
     * @param request 댓글 작성 요청 DTO
     * @param userId  현재 로그인한 사용자 ID (JWT에서 추출됨)
     * @return 작성된 댓글 응답 DTO
     */
    public CommentResponse createComment(CommentRequest request, String userId) {
        // 사용자 검증: userId → UsersEntity
        UsersEntity user = userService.findByIdWithThrow(userId);

        // 댓글 저장 처리 (Service 계층에서 트랜잭션 수행)
        CommentEntity comment = commentService.saveComment(
                request.getPostId(),
                request.getParentCmtId(),
                request.getCmtContent(),
                user
        );

        // 게시글 작성자인지 여부 판별
        boolean isPostWriter = comment.getPost().getUsersEntity().getUserId().equals(userId);

        // Entity → DTO 변환 및 반환
        return commentConvertor.toResponse(comment, isPostWriter);
    }

    /**
     * 특정 게시글에 작성된 댓글 목록(계층 구조 포함) 조회
     *
     * @param postId 대상 게시글 ID
     * @param userId 현재 요청자 ID (게시글 작성자 여부 판단용)
     * @return 계층형 댓글 응답 리스트
     */
    public List<CommentResponse> getComments(Long postId, String userId) {
        return commentService.getCommentsByPostId(postId, userId);
    }

    /**
     * 댓글 또는 대댓글 삭제 요청 처리
     *
     * - 댓글 작성자 또는 관리자만 삭제 가능
     * - 실제 삭제 대신 상태만 변경 (Soft Delete)
     *
     * @param cmtId  삭제 대상 댓글 ID
     * @param userId 현재 요청자 ID (JWT로 인증된 사용자)
     */
    public void deleteComment(Long cmtId, String userId) {
        // 요청자 유효성 검증
        UsersEntity requester = userService.findByIdWithThrow(userId);

        // 댓글 삭제 처리
        commentService.deleteComment(cmtId, requester);
    }
}
