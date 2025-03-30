package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.PostStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PostDetailResponse {

    /**
     * 게시글 상세 조회용 DTO
     * 사용자에게 상세한 글 정보를 보여줌
     */
    private Long postId;             // 게시글 ID
    private String postTitle;        // 제목
    private String postContent;      // 본문 내용
    private LocalDateTime postCreate; // 작성일
    private LocalDateTime postUpdate; // 수정일
    private PostStatus postStatus;   // 게시글 상태 (CREATED 등)

    private Long boardId;            // 게시판 ID
    private String category;         // 게시판 이름
    private String userNickName;     // 작성자 닉네임
    private List<String> imageUrls;  // 이미지 URL 리스트
    private int likeCount; // 추천 수 (상세 페이지에서도 보여줌)
    private int viewCount; // 게시글 조회수
}