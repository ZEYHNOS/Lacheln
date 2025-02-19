package aba3.lucid.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "language")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageEntity {

    @Id
    private String countryId;

    //국가이름
    private String countryName;

    //국가이미지URL
    private String countryUrl;
}
