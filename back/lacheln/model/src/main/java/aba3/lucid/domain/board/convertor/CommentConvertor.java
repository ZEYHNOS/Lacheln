package aba3.lucid.domain.board.convertor;

import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * CommentEntity를 응답용 DTO로 변환하는 역할
 */
@Component
public class CommentConvertor {

    /**
     * 댓글 Entity → 응답 DTO로 변환
     * @param comment 댓글 엔티티
     * @param isPostWriter 작성자가 게시글 작성자인지 여부
     * @return 응답용 DTO
     */
    public CommentResponse toResponse(CommentEntity comment, boolean isPostWriter) {
        return CommentResponse.builder()
                .cmtId(comment.getCmtId()) // 댓글 ID
                .parentCmtId(comment.getParent() != null ? comment.getParent().getCmtId() : null) // 부모 댓글 ID
                .userNickName(comment.getUsers().getUserNickName()) // 작성자 닉네임
                .cmtContent(comment.getCmtContent()) // 댓글 내용
                .cmtCreate(comment.getCmtCreate()) // 작성 시간
                .isPostWriter(isPostWriter) // 게시글 작성자인지 여부
                .cmtDegree(comment.getCmtDegree()) // 댓글 차수
                .children(new ArrayList<>()) // 기본은 비어 있는 자식 리스트
                .build();
    }
}