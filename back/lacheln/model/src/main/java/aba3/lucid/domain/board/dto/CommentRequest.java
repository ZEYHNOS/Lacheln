package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * 댓글 또는 대댓글 작성 시 클라이언트가 보내는 요청 형식
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentRequest {

    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId; // 댓글을 달 게시글의 ID

    private Long parentCmtId; // 부모 댓글 ID (null이면 일반 댓글, 값이 있으면 대댓글)

    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    private String cmtContent; // 댓글/답글의 실제 내용
}