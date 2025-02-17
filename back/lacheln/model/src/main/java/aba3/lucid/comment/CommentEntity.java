package aba3.lucid.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmtId;

    //부모답글Id
    private Long parentId;

    //게시글Id
    private Long postId;

    //사용자Id 이메일+비밀번호+비밀키
    private String userId;

    //내용
    private String cmtContent;

    //작성일
    private LocalTime cmtCreate;

    //수정일
    private LocalTime cmtUpdate;

    //상태 등록,비밀,삭제,관리자삭제,비밀수정
    private String cmtStatus;

    //차수
    private int filed;
}
