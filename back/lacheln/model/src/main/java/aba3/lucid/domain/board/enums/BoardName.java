package aba3.lucid.domain.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardName {

    FREE_BOARD("자유게시판"),
    QUESTION_BOARD("질문게시판"),
    POPULAR_BOARD("인기게시판"),
    REVIEW_BOARD("후기게시판");

    private final String description;
}
