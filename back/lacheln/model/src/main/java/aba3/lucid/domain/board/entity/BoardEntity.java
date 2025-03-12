package aba3.lucid.domain.board.entity;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.domain.board.enums.BoardName;
import aba3.lucid.domain.country.entity.CountryEntity;
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
    private int boardId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private CountryEntity country; // 게시판 국가 설정

    @Enumerated(EnumType.STRING)
    @Column(name = "board_name", columnDefinition = "CHAR(20)", nullable = false)
    private BoardName boardName; //게시판이름

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<PostEntity> postList;
}
