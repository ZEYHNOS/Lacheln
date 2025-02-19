package aba3.lucid.domain.board;

import aba3.lucid.domain.board.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(name = "board_id", nullable = false)
    private long boardId; //게시판ID

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId; //소비자ID

    @Column(name = "post_title", columnDefinition = "CHAR(100)", nullable = false)
    private String postTitle; //제목

    @Column(name = "post_content", columnDefinition = "LONGTEXT", nullable = false)
    private String postContent; // 내용

    @Column(name = "post_create", nullable = false)
    private LocalDateTime postCreate; //작성일

    @Column(name = "post_update", nullable = false)
    private LocalDateTime postUpdate; //수정일

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", columnDefinition = "CHAR(20)", nullable = false)
    private PostStatus postStatus; //상태 enum 참고
}
