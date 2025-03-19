package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PostResponse {

    private Long postId;    //게시글 ID
    private String postTitle;   //제목
    private String postContent; //내용
    private LocalDateTime postCreate;   //작성일(수정 시 변경X, 정렬 시 기준)
    private LocalDateTime postUpdate;   //수정일
    private PostStatus postStatus; //게시글 상태(등록, 수정, 상태 등)

    private Long BoardId;   //게시판 ID
    private String category;    //게시판 이름

    private String userId;    //작성자 ID
    private List<String> imageUrls; //첨부된 이미지 URL 리스트
}
