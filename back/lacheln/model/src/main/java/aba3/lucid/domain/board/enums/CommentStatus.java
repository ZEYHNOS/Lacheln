package aba3.lucid.domain.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentStatus {
    CREATED("등록"),        // 댓글 등록
    SECRET("비밀"),        // 비밀 댓글
    DELETED("삭제"),        // 일반 삭제
    ADMIN_DELETED("관리자 삭제"), // 관리자 삭제
    SECRET_UPDATED("비밀 수정");  // 비밀 댓글 수정

    private final String description;
}
