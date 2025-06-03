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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "board_name", length = 20, nullable = false, unique = true)
    private String boardName; // 게시판 이름 (예: 자유게시판, 질문게시판 등)

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> postList;

    // 게시판 이름 변경 메서드
    public void changeBoardName(String newName) {
        this.boardName = newName;
    }
}
