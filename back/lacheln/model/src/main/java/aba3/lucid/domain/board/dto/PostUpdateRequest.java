package aba3.lucid.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostUpdateRequest {

    @NotNull(message = "게시글 ID는 필수입니다.")
    @JsonProperty("post_id")
    private Long postId;

    @NotBlank(message = "제목은 필수입니다.")
    @JsonProperty("post_title")
    private String postTitle;

    @NotBlank(message = "본문은 필수입니다.")
    @JsonProperty("post_content")
    private String postContent;

    @NotNull(message = "게시판 ID는 필수입니다.")
    @JsonProperty("board_id")
    private Long boardId;
}

