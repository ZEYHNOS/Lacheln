package aba3.lucid.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {

    private String content;

    private Double score;

    private List<String> reviewImageUrl;

}
