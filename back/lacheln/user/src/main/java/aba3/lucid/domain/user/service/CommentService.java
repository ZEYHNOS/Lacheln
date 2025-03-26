package aba3.lucid.domain.board.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.entity.*;
import aba3.lucid.domain.board.enums.CommentStatus;
import aba3.lucid.domain.board.repository.CommentRepository;
import aba3.lucid.domain.board.repository.PostRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;

    /**
     * 댓글 또는 대댓글 저장
     *
     * @param postId        대상 게시글 ID
     * @param parentCmtId   부모 댓글 ID (null이면 일반 댓글)
     * @param content       댓글 내용
     * @param userId        작성자 ID
     * @return 저장된 CommentEntity
     */
    @Transactional
    public CommentEntity saveComment(Long postId, Long parentCmtId, String content, String userId) {
        // 게시글 조회
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        // 작성자 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));

        CommentEntity parent = null;
        int degree = 1; // 기본 댓글 차수는 1

        // 부모 댓글이 있는 경우 → 대댓글
        if (parentCmtId != null) {
            parent = commentRepository.findById(parentCmtId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "부모 댓글이 존재하지 않습니다."));
            degree = parent.getCmtDegree() + 1;

            // 차수 제한: 최대 4까지만 허용
            if (degree > 4) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "대댓글은 4개까지만 허용됩니다.");
            }
        }

        // 댓글 Entity 생성 및 저장
        CommentEntity comment = CommentEntity.builder()
                .post(post)                  // 어떤 게시글에 단 댓글인지
                .users(user)                // 댓글 작성자
                .cmtContent(content)        // 내용
                .parent(parent)             // 부모 댓글 (null이면 일반 댓글)
                .cmtDegree(degree)          // 댓글 차수
                .cmtStatus(CommentStatus.CREATED) // 댓글 상태
                .build();

        return commentRepository.save(comment);
    }
}