package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.PostStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostDetailResponse {

    /**
     * 게시글 상세 조회용 DTO
     * 게시글의 상세한 내용을 조회 하기 위해서 사용
     */
    private Long postId;
    private String postTitle;
    private String postContent;
    private LocalDateTime postCreate;
    private LocalDateTime postUpdate;
    private PostStatus postStatus;

    private Long boardId;
    private String category;
    private String userId;
    private List<String> imageUrls;
}
