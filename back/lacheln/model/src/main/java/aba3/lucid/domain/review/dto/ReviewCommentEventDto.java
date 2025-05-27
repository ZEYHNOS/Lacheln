package aba3.lucid.domain.review.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentEventDto {
    private Long reviewId;
    private Long cpId;
    private String payId;
}
