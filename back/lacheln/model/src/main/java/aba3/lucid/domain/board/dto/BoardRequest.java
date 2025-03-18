package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardRequest {

    @NotBlank(message = "게시판 이름은 필수입니다.")
    private String boardName;
}
