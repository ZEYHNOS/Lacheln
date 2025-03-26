package aba3.lucid.domain.company.entity;

import aba3.lucid.domain.user.enums.CountryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cp_country")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "cp_country" )
    private CountryEnum country;

}
