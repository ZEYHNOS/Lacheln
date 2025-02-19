package aba3.lucid.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "prefer_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferCategoryEntity {

    @Id
    private String category; //스, 드, 메

    //소비자ID
    private String userId;
}
