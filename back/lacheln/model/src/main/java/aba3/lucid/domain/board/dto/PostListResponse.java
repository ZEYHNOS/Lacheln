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
     * 게시판 목록 조회용 DTO
     * 사용자에게 보여줄 간략한 게시글 정보
     */
    private Long postId;             // 게시글 ID
    private String postTitle;        // 제목
    private String userNickName;     // 작성자 닉네임 (UUID는 노출하지 않음)
    private String category;         // 게시판 이름
    private LocalDateTime postCreate; // 작성일
    private int viewCount;           // 조회수
    private int likeCount;           // 추천수
    private int commentCount;
}