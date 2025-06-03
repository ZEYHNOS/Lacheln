package aba3.lucid.domain.board.convertor;

import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.board.dto.CommentResponse;
import aba3.lucid.domain.board.entity.CommentEntity;
import aba3.lucid.domain.board.enums.CommentStatus;

import java.util.ArrayList;

/**
 * 댓글(CommentEntity)을 클라이언트에게 전달할 응답 DTO(CommentResponse)로 변환하는 역할
 *
 * - 게시글 작성자 여부 표시
 * - 자식 댓글(children)을 위한 빈 리스트 초기화
 * - 삭제/비밀 댓글의 경우 내용 가공 처리 ("삭제된 댓글입니다", "비밀 댓글입니다")
 */
@Converter
public class CommentConvertor {

    /**
     * 댓글 Entity를 응답용 DTO로 변환하는 메서드
     *
     * @param comment 댓글 엔티티 객체
     * @param isPostWriter 이 댓글 작성자가 해당 게시글의 작성자인지 여부
     * @return 클라이언트에게 전달할 댓글 응답 DTO
     */
    public CommentResponse toResponse(CommentEntity comment, boolean isPostWriter) {

        // 1. 댓글 상태에 따른 내용 가공 처리
        String displayedContent;
        CommentStatus status = comment.getCmtStatus();

        if (status == CommentStatus.DELETED || status == CommentStatus.ADMIN_DELETED) {
            displayedContent = "삭제된 댓글입니다";
        } else if (status == CommentStatus.SECRET || status == CommentStatus.SECRET_UPDATED) {
            displayedContent = "비밀 댓글입니다";
        } else {
            displayedContent = comment.getCmtContent();
        }

        // 2. DTO 구성 및 반환
        return CommentResponse.builder()
                .cmtId(comment.getCmtId()) // 댓글 ID
                .parentCmtId(comment.getParent() != null ? comment.getParent().getCmtId() : null) // 부모 댓글 ID
                .userNickName(comment.getUsers().getUserNickName()) // 작성자 닉네임
                .cmtContent(displayedContent) // 상태에 따라 가공된 댓글 내용
                .cmtCreate(comment.getCmtCreate()) // 작성 시간
                .isPostWriter(isPostWriter) // 게시글 작성자인지 여부
                .cmtDegree(comment.getCmtDegree()) // 댓글 차수
                .children(new ArrayList<>()) // 자식 댓글 초기화 (계층 구조 지원)
                .build();
    }
}
