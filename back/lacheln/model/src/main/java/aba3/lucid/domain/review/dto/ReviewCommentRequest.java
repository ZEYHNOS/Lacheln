package aba3.lucid.domain.review.dto;


import aba3.lucid.domain.product.enums.CommentStatus;
import aba3.lucid.domain.product.enums.ReviewStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewCommentRequest {

    private String userId;

    private String content;

}
