package aba3.lucid.hashtag.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "hashtag")
public class HashTagEntity {

    // 태그 ID(tagName + pdId로 복합키 사용 예정)
    @Id
    private String tagName;

    // 외래키
    private Long pdId;
}
