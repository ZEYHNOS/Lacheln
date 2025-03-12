package aba3.lucid.domain.company.entity;

import aba3.lucid.domain.country.entity.CountryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cp_country_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyCountryEntity {

    @Id
    @Column(name = "cp_country_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpCountryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id" )
    private CountryEntity country;

}
