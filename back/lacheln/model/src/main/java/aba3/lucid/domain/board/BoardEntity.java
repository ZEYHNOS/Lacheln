package aba3.lucid.domain.board;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.domain.user.CountryEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.List;

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
    private int boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    private CountryEntity country; // 게시판 국가 설정

    @Column(name = "board_name", length = 50, nullable = false)
    private String boardName; //게시판이름

    @Enumerated(EnumType.STRING)
    @Column(name = "board_status", columnDefinition = "CHAR(20)", nullable = false)
    private ActiveEnum boardStatus; //활성화여부 enum 참고

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<PostEntity> postList;
}
