// ✅ 리팩토링된 CommentBusiness.java
package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.TierEnum;
import aba3.lucid.board.service.CommentService;
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
    private final UserService userService;

    /**
     * ✅ 댓글 또는 답글 작성
     *
     * - 세미프로 이상 등급만 가능
     * - 게시글 ID와 부모 댓글 ID(parentCmtId)가 전달됨
     */
    public CommentResponse createComment(CommentRequest request, String userId) {
        // 1. 사용자 정보 조회
        UsersEntity user = userService.findByIdWithThrow(userId);

        // 2. 작성 권한 검증 (세미프로 이상만 댓글 가능)
        validateWritableTier(user.getUserTier());

        // 3. 댓글 저장 처리 (PostEntity, ParentCmt 포함)
        CommentEntity comment = commentService.saveComment(
                request.getPostId(),
                request.getParentCmtId(),
                request.getCmtContent(),
                user
        );

        // 4. 해당 댓글 작성자가 게시글 작성자인지 여부 판단
        boolean isPostWriter = comment.getPost().getUsersEntity().getUserId().equals(userId);

        // 5. DTO로 변환하여 반환
        return commentConvertor.toResponse(comment, isPostWriter);
    }

    /**
     * ✅ 게시글 댓글 목록 조회 (비회원 포함)
     *
     * - 인증되지 않은 경우 userId는 null로 들어옴
     * - userId가 존재하면 "해당 댓글이 게시글 작성자인가" 등의 표시를 위한 참고용
     * - 댓글은 계층 구조로 정렬되어 반환됨
     */
    public List<CommentResponse> getComments(Long postId, String userId) {
        return commentService.getCommentsByPostId(postId, userId);
    }

    /**
     * ✅ 댓글 삭제
     *
     * - 작성자 본인 또는 관리자만 삭제 가능
     * - 자식 댓글이 있는 경우 함께 soft delete 처리
     */
    public void deleteComment(Long cmtId, String userId) {
        // 1. 사용자 조회 (삭제 권한 확인 필요)
        UsersEntity user = userService.findByIdWithThrow(userId);

        // 2. 삭제 로직 위임
        commentService.deleteComment(cmtId, user);
    }

    /**
     * ✅ 댓글 작성 가능 등급 확인
     *
     * - 세미프로 이상만 댓글 작성 가능
     * - AMATEUR 및 GUEST는 제한됨
     */
    private void validateWritableTier(TierEnum tier) {
        if (tier == null || tier.ordinal() < TierEnum.SEMI_PRO.ordinal()) {
            throw new IllegalArgumentException("세미프로 등급 이상만 댓글을 작성할 수 있습니다.");
        }
    }
}
