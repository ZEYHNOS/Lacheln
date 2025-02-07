package aba3.lucid.board.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO 태호형
@Getter
@Entity(name = "board")
@AllArgsConstructor
@NoArgsConstructor
// TODO @Builder 넣기
public class BoardEntity {

    // todo 자동증가
    @Id
    private int boardId;

    private String countryId;

    private String boardName;

    private String boardStatus;
}
