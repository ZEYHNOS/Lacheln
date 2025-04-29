package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.PostStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 게시글 상세 조회용 DTO
 * 사용자에게 상세한 글 정보를 보여줍니다.
 * (본문에 <img> 태그가 삽입되어 있을 수 있습니다)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PostDetailResponse {

    private Long postId;             // 게시글 ID
    private String postTitle;        // 제목
    private String postContent;      // 본문 내용 (이미지 <img> 태그 포함 가능)
    private LocalDateTime postCreate; // 작성일
    private LocalDateTime postUpdate; // 수정일
    private PostStatus postStatus;   // 게시글 상태 (CREATED, UPDATED 등)

    private Long boardId;            // 게시판 ID
    private String category;         // 게시판 이름
    private String userNickName;     // 작성자 닉네임

    private int likeCount;           // 추천 수
    private int viewCount;           // 조회 수
}
