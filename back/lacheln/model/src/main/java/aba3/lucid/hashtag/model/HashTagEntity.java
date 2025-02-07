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

    // TODO 태그 ID(tagName + pdId로 복합키 사용 예정) --> EmbeddedId 사용?
    @Id
    private String tagName;

    // TODO 외래키 설정
    private Long pdId;
}
