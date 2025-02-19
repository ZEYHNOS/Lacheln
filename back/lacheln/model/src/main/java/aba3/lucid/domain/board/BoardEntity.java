package aba3.lucid.domain.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    //국가코드
    private String countryId;

    //게시판이름
    private String boardName;

    //활성화여부 0비활성화 1활성화
    private String boardStatus;
}
