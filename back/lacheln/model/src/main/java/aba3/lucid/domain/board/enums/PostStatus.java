package aba3.lucid.domain.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    CREATED("등록"),       // 글 등록
    UPDATED("수정"),       // 글 수정
    DELETED("삭제"),       // 사용자가 삭제한 글
    ADMIN_DELETED("관리자 삭제"); // 관리자가 삭제한 글

    private final String description;
}