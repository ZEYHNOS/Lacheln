package aba3.lucid.board.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "board")
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {

    @Id
    private int boardId;

    private String countryId;

    private String boardName;

    private String boardStatus;
}
