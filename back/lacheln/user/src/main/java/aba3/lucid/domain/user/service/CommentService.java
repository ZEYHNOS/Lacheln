package aba3.lucid.domain.board.service;

import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.board.convertor.CommentConvertor;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.*;
import aba3.lucid.domain.board.enums.CommentStatus;
import aba3.lucid.domain.board.repository.CommentRepository;
import aba3.lucid.domain.board.repository.PostRepository;
import aba3.lucid.domain.user.entity.UsersEntity;
import aba3.lucid.domain.user.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final CommentConvertor commentConvertor;

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

    /**
     * 특정 게시글에 달린 모든 댓글 및 대댓글을 계층 구조로 조회
     *
     * - 1차로 DB에서 모든 댓글을 조회한 후, 메모리에서 부모-자식 구조로 조립
     * - 댓글의 parent가 null이면 부모 댓글
     * - parent가 존재하면 해당 댓글의 자식 댓글로 children 리스트에 포함
     * - 댓글 작성자가 게시글 작성자인지 여부도 함께 판단해 응답에 포함
     *
     * @param postId 댓글이 달린 게시글 ID
     * @param userId 현재 요청자의 ID (작성자 여부 판별용)
     * @return 계층 구조로 구성된 댓글 응답 DTO 리스트
     */
    @Transactional
    public List<CommentResponse> getCommentsByPostId(Long postId, String userId) {
        // 1. 해당 게시글이 존재하는지 확인 (삭제된 글은 제외)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));

        // 2. 댓글 목록 전체 조회 (작성 시간 순)
        List<CommentEntity> comments = commentRepository.findByPost_PostIdOrderByCmtCreateAsc(postId);

        // 3. 응답 DTO를 Map에 저장하여 빠르게 참조할 수 있도록 구성
        Map<Long, CommentResponse> commentMap = new HashMap<>();
        List<CommentResponse> result = new ArrayList<>();

        for (CommentEntity comment : comments) {
            // 3-1. 댓글 작성자가 게시글 작성자인지 비교
            boolean isPostWriter = comment.getUsers().getUserId().equals(post.getUsersEntity().getUserId());

            // 3-2. Entity → DTO 변환
            CommentResponse response = commentConvertor.toResponse(comment, isPostWriter);
            commentMap.put(comment.getCmtId(), response);

            // 3-3. 부모 댓글이면 결과 리스트에 추가
            if (comment.getParent() == null) {
                result.add(response);
            } else {
                // 3-4. 자식 댓글이면 부모의 children 리스트에 추가
                CommentResponse parentResponse = commentMap.get(comment.getParent().getCmtId());
                if (parentResponse != null) {
                    parentResponse.getChildren().add(response);
                }
            }
        }

        return result;
    }
}