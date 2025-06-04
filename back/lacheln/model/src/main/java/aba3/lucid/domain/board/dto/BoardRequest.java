package aba3.lucid.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardRequest {

    @NotBlank(message = "게시판 이름은 필수입니다.")
    @JsonProperty("board_name")
    private String boardName;
}
