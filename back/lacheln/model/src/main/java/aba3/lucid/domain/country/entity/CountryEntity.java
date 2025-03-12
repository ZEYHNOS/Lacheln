package aba3.lucid.domain.country.entity;

import aba3.lucid.domain.user.enums.CountryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryEntity {

    @Id
    @Column(name = "country_id", columnDefinition = "CHAR(2)")
    @Enumerated(EnumType.STRING)
    private CountryEnum countryId; //국가명코드 ISO 3166-1 alpha-2

    @Column(name = "country_name", length = 100, nullable = false)
    private String countryName;

    @Column(name = "country_url", length = 255, nullable = false)
    private String countryUrl;
}
