package aba3.lucid.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "hashtag")
@AllArgsConstructor
@NoArgsConstructor
public class HashtagEntity {

    // 기본키 - type(varchar(10))
    @Id
    private String tagName;

    // 상품 ID(외래키)
    @Column(nullable = false)
    private Long pdId;
}
