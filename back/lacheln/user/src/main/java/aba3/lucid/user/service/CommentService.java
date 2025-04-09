package aba3.lucid.user.service;

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
     * - 게시글과 사용자 정보를 확인 후 댓글 저장
     * - 부모 댓글이 존재할 경우: 대댓글로 처리
     * - 대댓글은 최대 4단계까지만 허용
     * - 삭제된 부모 댓글에는 대댓글 작성 불가
     *
     * @param postId        대상 게시글 ID
     * @param parentCmtId   부모 댓글 ID (null이면 일반 댓글)
     * @param content       댓글 내용
     * @param userId        작성자 ID
     * @return 저장된 CommentEntity
     */
    @Transactional
    public CommentEntity saveComment(Long postId, Long parentCmtId, String content, String userId) {
        // 1. 게시글 존재 여부 확인 (삭제된 게시글은 예외 처리)
        PostEntity post = postRepository.findByPostIdAndDeletedFalse(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 게시글이 존재하지 않거나 삭제되었습니다."));

        // 2. 작성자(사용자) 정보 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 사용자가 존재하지 않습니다."));

        CommentEntity parent = null; // 부모 댓글 (대댓글일 경우 필요)
        int degree = 1; // 기본 댓글 차수는 1 (일반 댓글)

        // 3. 부모 댓글이 존재할 경우 → 대댓글 작성 흐름
        if (parentCmtId != null) {
            parent = commentRepository.findById(parentCmtId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "부모 댓글이 존재하지 않습니다."));

            // 삭제된 댓글에는 대댓글 작성 불가
            if (parent.getCmtStatus() == CommentStatus.DELETED) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "삭제된 댓글에는 대댓글을 작성할 수 없습니다.");
            }

            // 대댓글 차수 계산 (부모 차수 + 1)
            degree = parent.getCmtDegree() + 1;

            // 최대 4단계까지만 허용 (5단계 이상은 차단)
            if (degree > 4) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "대댓글은 최대 4단계까지만 허용됩니다.");
            }
        }

        // 4. 댓글 Entity 생성 및 저장
        CommentEntity comment = CommentEntity.builder()
                .post(post)                           // 소속 게시글
                .users(user)                         // 댓글 작성자
                .cmtContent(content)                 // 내용
                .parent(parent)                      // 부모 댓글 (없으면 null)
                .cmtDegree(degree)                   // 차수 설정 (1~4)
                .cmtStatus(CommentStatus.CREATED)    // 기본 상태는 CREATED
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

    /**
     * 댓글 또는 대댓글 삭제 (Soft Delete 방식)
     *
     * - 삭제는 댓글 작성자 또는 운영자(관리자 권한)가 가능함
     * - 실제로 데이터를 삭제하지 않고, 상태만 DELETED로 변경
     * - 대댓글은 별도로 삭제하지 않으며 그대로 유지
     *
     * @param cmtId   삭제할 댓글 ID
     * @param userId  삭제 요청자 ID
     */
    @Transactional
    public void deleteComment(Long cmtId, String userId) {
        // 1. 댓글 존재 여부 확인
        CommentEntity comment = commentRepository.findById(cmtId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));

        // 2. 작성자와 관리자 여부 확인
        UsersEntity writer = comment.getUsers();
        UsersEntity requester = usersRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "삭제 요청자가 존재하지 않습니다."));

        boolean isWriter = writer.getUserId().equals(requester.getUserId());
        boolean isAdmin = requester.getUserTier().toString().equalsIgnoreCase("ADMIN"); // 등급 기반 관리자 판단

        if (!isWriter && !isAdmin) {
            throw new ApiException(ErrorCode.FORBIDDEN, "해당 댓글을 삭제할 권한이 없습니다.");
        }

        // 3. 댓글 상태를 DELETED로 변경 (Soft Delete)
        comment.delete();
    }
}