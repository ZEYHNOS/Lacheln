package aba3.lucid.language;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "language")
public class LanguageEntity {
    @Id
    private String country_Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;
}
