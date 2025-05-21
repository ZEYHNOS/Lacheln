package aba3.lucid.domain.review.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자에게 보여줄 리뷰 응답 DTO
 * - 리뷰 ID는 노출하지 않음
 * - 작성자 닉네임, 리뷰 내용, 평점, 이미지, 작성일시만 포함
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewResponse {

    private String nickname;                // 작성자 닉네임
    private String content;                 // 리뷰 내용
    private Double score;                   // 리뷰 평점
    private List<String> imageUrls;         // 첨부 이미지 URL 리스트
    private LocalDateTime createdAt;        // 작성일시
    private String replyContent;       // 판매자가 작성한 답글 내용
    private LocalDate replyCreatedAt;  // 답글 작성일 (삭제된 경우 null)

}
