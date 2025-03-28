package aba3.lucid.domain.board.entity;

import aba3.lucid.domain.user.enums.CountryEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

//    @Enumerated(EnumType.STRING)
//    private CountryEnum country; // 게시판 국가 설정

    @Column(name = "board_name", length = 20, nullable = false)
    private String boardName; //게시판이름

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> postList;

    //게시판 이름 수정
    public void changeBoardName(String newName) {
        this.boardName = newName;
    }
}
