package aba3.lucid.domain.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    //게시판ID
    private Long boardId;

    //소비자ID
    private String userId;

    //제목
    private String postTitle;

    //내용
    private String postContent;

    //작성일
    private LocalTime postCreate;

    //수정일
    private LocalTime postUpdate;

    //상태 등록, 삭제, 수정, 관리자삭제
    private String postStatus;
}
