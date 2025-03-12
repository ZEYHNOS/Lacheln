package aba3.lucid.domain.board.dto;

import aba3.lucid.domain.board.enums.BoardName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequest {

    private String countryId;
    private BoardName boardName;
}
