package aba3.lucid.domain.board;

import aba3.lucid.common.enums.ActiveEnum;
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
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @Column(name = "country_id", columnDefinition = "CHAR(3)", nullable = false)
    private String country; //국가코드

    @Column(name = "board_name", length = 50, nullable = false)
    private String boardName; //게시판이름

    @Enumerated(EnumType.STRING)
    @Column(name = "board_status", columnDefinition = "CHAR(20)", nullable = false)
    private ActiveEnum boardStatus; //활성화여부 enum 참고
}
