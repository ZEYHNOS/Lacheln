package aba3.lucid.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 또는 대댓글을 사용자에게 보여줄 때 사용하는 응답 형식
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponse {

    private Long cmtId;             // 댓글 ID (PK)
    private Long parentCmtId;       // 부모 댓글 ID (없으면 null → 일반 댓글)
    private String userNickName;    // 작성자의 닉네임
    private String userId;
    private String cmtContent;      // 댓글/답글의 내용
    private LocalDateTime cmtCreate; // 작성 시간
    private boolean isPostWriter;   // 해당 댓글 작성자가 게시글 작성자인지 여부
    private int cmtDegree;          // 댓글 차수 (댓글: 1, 대댓글: 2~4)
    private List<CommentResponse> children; // 자식 댓글(대댓글) 목록
}