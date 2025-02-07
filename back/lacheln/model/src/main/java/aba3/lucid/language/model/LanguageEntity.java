package aba3.lucid.language.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// todo 현준이형
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "language")
// todo @Builder 넣어주세요
public class LanguageEntity {
    @Id
    private String countryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;
}
