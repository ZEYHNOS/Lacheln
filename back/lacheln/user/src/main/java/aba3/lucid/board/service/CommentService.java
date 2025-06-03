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
     */
    @Transactional
    public CommentEntity saveComment(Long postId, Long parentCmtId, String content, UsersEntity user) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        CommentEntity parent = null;
        int degree = 1;

        if (parentCmtId != null) {
            parent = commentRepository.findById(parentCmtId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "부모 댓글이 존재하지 않습니다."));

            if (parent.getCmtStatus() == CommentStatus.DELETED) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "삭제된 댓글에는 대댓글을 작성할 수 없습니다.");
            }

            degree = parent.getCmtDegree() + 1;
            if (degree > 4) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "대댓글은 최대 4단계까지만 허용됩니다.");
            }
        }

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
     * 게시글의 댓글/답글 목록 조회 (계층 구조 포함)
     */
    @Transactional
    public List<CommentResponse> getCommentsByPostId(Long postId, String userId) {
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        List<CommentEntity> comments = commentRepository.findByPost_PostIdOrderByCmtCreateAsc(postId);

        Map<Long, CommentResponse> commentMap = new HashMap<>();
        List<CommentResponse> result = new ArrayList<>();

        for (CommentEntity comment : comments) {
            boolean isPostWriter = comment.getUsers().getUserId().equals(post.getUsersEntity().getUserId());
            CommentResponse response = commentConvertor.toResponse(comment, isPostWriter);
            commentMap.put(comment.getCmtId(), response);

            if (comment.getParent() == null) {
                result.add(response);
            } else {
                CommentResponse parent = commentMap.get(comment.getParent().getCmtId());
                if (parent != null) {
                    parent.getChildren().add(response);
                }
            }
        }

        return result;
    }

    /**
     * 댓글 삭제 (작성자 또는 ADMIN만 가능, 자식 댓글도 함께 Soft Delete)
     */
    @Transactional
    public void deleteComment(Long cmtId, UsersEntity user) {
        CommentEntity comment = commentRepository.findById(cmtId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        boolean isWriter = comment.getUsers().getUserId().equals(user.getUserId());
        boolean isAdmin = user.getUserTier().toString().equalsIgnoreCase("ADMIN");

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "해당 댓글을 삭제할 권한이 없습니다.");
        }

        deleteRecursively(comment); // ✅ 자식 댓글까지 함께 삭제
    }

    /**
     * 자식 댓글 포함 재귀 Soft Delete
     */
    private void deleteRecursively(CommentEntity comment) {
        if (comment.getChildren() != null) {
            for (CommentEntity child : comment.getChildren()) {
                deleteRecursively(child); // 자식도 재귀 삭제
            }
        }

        comment.deleteWithChildren();
    }
}