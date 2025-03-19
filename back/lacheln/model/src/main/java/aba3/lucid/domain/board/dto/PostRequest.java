package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle;
}
