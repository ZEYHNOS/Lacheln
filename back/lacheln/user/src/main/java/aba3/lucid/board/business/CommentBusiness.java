package aba3.lucid.board.business;

import aba3.lucid.common.annotation.Business;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentRequest;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.enums.TierEnum; // ✅ enum import 경로 수정
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
     * 댓글 또는 답글 작성
     *
     * @param request 댓글 작성 요청
     * @param userId  JWT에서 추출된 사용자 ID
     * @return 작성된 댓글 DTO
     */
    public CommentResponse createComment(CommentRequest request, String userId) {
        // 1. 사용자 정보 조회 및 권한 검증 (세미프로 이상만 가능)
        UsersEntity user = userService.findByIdWithThrow(userId);
        validateWritableTier(user.getUserTier());

        // 2. 댓글 저장 처리
        CommentEntity comment = commentService.saveComment(
                request.getPostId(),
                request.getParentCmtId(),
                request.getCmtContent(),
                user
        );

        // 3. 해당 댓글 작성자가 게시글 작성자인지 여부 판단
        boolean isPostWriter = comment.getPost().getUsersEntity().getUserId().equals(userId);

        // 4. DTO 변환 및 반환
        return commentConvertor.toResponse(comment, isPostWriter);
    }

    /**
     * 게시글 댓글 목록 조회
     *
     * @param postId 게시글 ID
     * @param userId JWT에서 추출된 사용자 ID
     * @return 계층형 댓글 응답 리스트
     */
    public List<CommentResponse> getComments(Long postId, String userId) {
        return commentService.getCommentsByPostId(postId, userId);
    }

    /**
     * 댓글 삭제
     *
     * @param cmtId  댓글 ID
     * @param userId JWT에서 추출된 사용자 ID
     */
    public void deleteComment(Long cmtId, String userId) {
        // 1. 사용자 조회
        UsersEntity user = userService.findByIdWithThrow(userId);

        // 2. 댓글 삭제 위임 (작성자 또는 ADMIN만 삭제 가능)
        commentService.deleteComment(cmtId, user);
    }

    /**
     * 세미프로 이상인지 확인 (댓글/답글 작성 가능)
     */
    private void validateWritableTier(TierEnum tier) {
        if (tier == null || tier.ordinal() < TierEnum.SEMI_PRO.ordinal()) {
            throw new IllegalArgumentException("세미프로 등급 이상만 댓글을 작성할 수 있습니다.");
        }
    }
}
