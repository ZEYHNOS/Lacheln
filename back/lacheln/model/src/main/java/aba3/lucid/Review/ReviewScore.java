package aba3.lucid.Review;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="review")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    private long scheduleId;

    private String rvContent;

    private String rvCreate;

    private String rvStatus; //등록 수정 삭제

    private String rvScore;
}
