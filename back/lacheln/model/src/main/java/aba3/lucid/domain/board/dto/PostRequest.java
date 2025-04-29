package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle; // 게시글 제목

    @NotBlank(message = "본문은 필수입니다. <img> 태그를 포함할 수 있습니다.")
    private String postContent; // 본문 내용 (이미지 포함 가능)

    @NotNull(message = "게시판 ID는 필수입니다.")
    private Long boardId; // 게시판 ID
}