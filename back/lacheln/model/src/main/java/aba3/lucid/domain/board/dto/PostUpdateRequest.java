package aba3.lucid.domain.board.dto;

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
    private Long postId;

    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle;

    @NotBlank(message = "본문은 필수입니다.")
    private String postContent; // 본문 내용 (이미지 포함된 HTML)
}
