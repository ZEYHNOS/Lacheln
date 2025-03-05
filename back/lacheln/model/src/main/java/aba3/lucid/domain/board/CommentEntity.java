package aba3.lucid.domain.board;

import aba3.lucid.domain.board.enums.CommentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @Column(name = "cmt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cmtId;

    @Column(name = "parent_id")
    private Long parent; //부모답글Id

    @Column(name = "post_id", nullable = false)
    private long post; //게시글Id

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String user; //사용자Id

    @Column(name = "cmt_content", columnDefinition = "CHAR(100)", nullable = false)
    private String cmtContent; //내용

    @Column(name = "cmt_create", nullable = false)
    private LocalDateTime cmtCreate; //작성일

    @Column(name = "cmt_update", nullable = false)
    private LocalDateTime cmtUpdate; //수정일

    @Enumerated(EnumType.STRING)
    @Column(name = "cmt_status", columnDefinition = "CHAR(20)", nullable = false)
    private CommentStatus cmtStatus; //CommentStatus 참고

    @Column(name = "cmt_degree", nullable = false)
    private int cmtDegree; //차수 1~4
}
