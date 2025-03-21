package aba3.lucid.domain.board.entity;

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
    private long boardId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private CountryEntity country; // 게시판 국가 설정

    @Column(name = "board_name", length = 20, nullable = false)
    private String boardName; //게시판이름

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<PostEntity> postList;

    //게시판 이름 수정
    public void changeBoardName(String newName) {
        this.boardName = newName;
    }
}
