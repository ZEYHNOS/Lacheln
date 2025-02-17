package aba3.lucid.Review;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="review_comment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewComment {
    @Id
    private long reviewId;

    private long cpId;

    private long scheduleId;

    private String rvcContent;  //답글 content

    private String rvcCreate;

    private String rvcStatus; // 답글  표시 숨기기 삭제




}
