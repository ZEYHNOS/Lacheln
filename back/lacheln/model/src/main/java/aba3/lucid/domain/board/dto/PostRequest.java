package aba3.lucid.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostRequest {

    @JsonProperty("post_title")
    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle;

    @JsonProperty("post_content")
    @NotBlank(message = "본문은 필수입니다.")
    private String postContent;

    @JsonProperty("board_id")
    @NotNull(message = "게시판 ID는 필수입니다.")
    private Long boardId;
}

