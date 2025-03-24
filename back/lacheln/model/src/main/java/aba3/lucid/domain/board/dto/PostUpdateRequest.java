package aba3.lucid.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PostUpdateRequest {

    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "제목은 필수입니다.")
    private String postTitle;

    @NotBlank(message = "본문은 필수입니다.")
    private String postContent;

    private List<String> imageUrls; // 새로 추가할 이미지 URL

    private List<String> deleteImageUrls; // 삭제할 기존 이미지 URL
}