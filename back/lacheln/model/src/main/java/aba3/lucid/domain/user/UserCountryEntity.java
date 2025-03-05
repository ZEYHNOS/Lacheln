package aba3.lucid.domain.user;


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
@Table(name = "country")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCountryEntity {
    @Id
    @Column(length = 2, nullable = false)
    private String countryId; // ISO 3166-1 alpha-2 코드

    @Column(nullable = false, length = 100)
    private String countryName;

    @Column(nullable = false, length = 255)
    private String countryUrl;



}
