package aba3.lucid.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostListResponse {

    /**
     * 게시판 조회용
     * 게시판에 글 리스트를 최대한 간략하게 뜨게 하기 위해서 간략화
     */
    private Long postId;         // 게시글 ID
    private String postTitle;    // 제목
    private String userId;       // 작성자 ID
    private String category;     // 게시판 이름
    private LocalDateTime postCreate; // 작성일
    private int viewCount;       // 조회수 (옵션: 아직 미구현이면 0)
    private int likeCount;       // 추천수 (옵션: 아직 미구현이면 0)
}
