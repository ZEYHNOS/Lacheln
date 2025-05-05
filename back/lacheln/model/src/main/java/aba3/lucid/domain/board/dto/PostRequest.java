package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle; //게시글 제목

    @NotBlank(message = "본문은 필수입니다.")
    private String postContent; //게시글 내용

    @NotNull(message = "게시판 ID는 필수입니다.")
    private Long boardId; //게시판 ID(글을 쓸 게시판 선택을 위해)ID

    private List<String> imageUrls; //이미지 URL 리스트
}
