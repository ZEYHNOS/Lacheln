package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.PostStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 게시글 상세 조회 응답 DTO
 * - 상세 페이지에서 사용자에게 게시글의 모든 정보를 보여줄 때 사용됩니다.
 * - 본문 내용(postContent)에는 이미지가 포함된 HTML 형식의 콘텐츠가 포함될 수 있습니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PostDetailResponse {

    private Long postId;             // 게시글 고유 ID
    private String postTitle;        // 게시글 제목
    private String postContent;      // 게시글 본문 (이미지 포함된 HTML 가능)
    private LocalDateTime postCreate; // 게시글 최초 작성 시각
    private LocalDateTime postUpdate; // 게시글 마지막 수정 시각
    private PostStatus postStatus;   // 게시글 상태 (CREATED, DELETED 등)

    private Long boardId;            // 게시판 ID (어떤 게시판에 속한 글인지 구분)
    private String category;         // 게시판 이름 (ex. 자유게시판, 질문게시판 등)
    private String userNickName;     // 게시글 작성자의 닉네임 (UUID 아님)

    private int likeCount;           // 해당 게시글의 추천 수
    private int viewCount;           // 해당 게시글의 조회 수
}
