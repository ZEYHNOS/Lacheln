package aba3.lucid.board.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.*;
import aba3.lucid.domain.board.enums.CommentStatus;
import aba3.lucid.domain.board.repository.CommentRepository;
import aba3.lucid.domain.board.repository.PostRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentConvertor commentConvertor;

    /**
     * 댓글 또는 대댓글 저장
     *
     * - 게시글 존재 여부 확인
     * - 부모 댓글 유효성 및 차수 제한 확인
     * - 댓글 저장 및 반환
     *
     * @param postId      게시글 ID
     * @param parentCmtId 부모 댓글 ID (null이면 일반 댓글)
     * @param content     댓글 내용
     * @param user        댓글 작성자 (UsersEntity)
     * @return 저장된 댓글 Entity
     */
    @Transactional
    public CommentEntity saveComment(Long postId, Long parentCmtId, String content, UsersEntity user) {
        // 1. 게시글 존재 여부 확인 (삭제된 게시글은 예외 처리)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        // 2. 대댓글 작성 여부에 따라 분기 처리
        CommentEntity parent = null;
        int degree = 1; // 일반 댓글 기본 차수

        if (parentCmtId != null) {
            parent = commentRepository.findById(parentCmtId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "부모 댓글이 존재하지 않습니다."));

            // 삭제된 부모 댓글에는 대댓글 작성 불가
            if (parent.getCmtStatus() == CommentStatus.DELETED) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "삭제된 댓글에는 대댓글을 작성할 수 없습니다.");
            }

            // 차수 제한 확인
            degree = parent.getCmtDegree() + 1;
            if (degree > 4) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "대댓글은 최대 4단계까지만 허용됩니다.");
            }
        }

        // 3. 댓글 Entity 생성 및 저장
        CommentEntity comment = CommentEntity.builder()
                .post(post)
                .users(user)
                .cmtContent(content)
                .parent(parent)
                .cmtDegree(degree)
                .cmtStatus(CommentStatus.CREATED)
                .build();

        return commentRepository.save(comment);
    }

    /**
     * 특정 게시글의 댓글/답글 목록을 계층 구조로 조회
     *
     * - 부모-자식 관계 조립
     *
     * @param postId 게시글 ID
     * @param userId 요청 사용자 ID (게시글 작성자 여부 확인용)
     * @return 계층형 댓글 리스트
     */
    @Transactional
    public List<CommentResponse> getCommentsByPostId(Long postId, String userId) {
        // 게시글 유효성 확인
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        // 전체 댓글 조회 (작성일 오름차순)
        List<CommentEntity> comments = commentRepository.findByPost_PostIdOrderByCmtCreateAsc(postId);

        // 계층 구조 조립
        Map<Long, CommentResponse> commentMap = new HashMap<>();
        List<CommentResponse> result = new ArrayList<>();

        for (CommentEntity comment : comments) {
            boolean isPostWriter = comment.getUsers().getUserId().equals(post.getUsersEntity().getUserId());

            CommentResponse response = commentConvertor.toResponse(comment, isPostWriter);
            commentMap.put(comment.getCmtId(), response);

            if (comment.getParent() == null) {
                result.add(response);
            } else {
                CommentResponse parentResponse = commentMap.get(comment.getParent().getCmtId());
                if (parentResponse != null) {
                    parentResponse.getChildren().add(response);
                }
            }
        }

        return result;
    }

    /**
     * 댓글 삭제 처리 (Soft Delete)
     *
     * - 작성자 또는 관리자만 삭제 가능
     * - 상태만 DELETED로 변경
     *
     * @param cmtId 삭제 대상 댓글 ID
     * @param user  삭제 요청자 (UsersEntity)
     */
    @Transactional
    public void deleteComment(Long cmtId, UsersEntity user) {
        // 댓글 존재 여부 확인
        CommentEntity comment = commentRepository.findById(cmtId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        // 삭제 권한 검증
        boolean isWriter = comment.getUsers().getUserId().equals(user.getUserId());
        boolean isAdmin = user.getUserTier().toString().equalsIgnoreCase("ADMIN");

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "해당 댓글을 삭제할 권한이 없습니다.");
        }

        // Soft Delete 처리
        comment.delete();
    }
}
