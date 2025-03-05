package aba3.lucid.domain.company;

import aba3.lucid.domain.user.UserCountryEntity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpCountryId;

    // TODO 관계 이미 다 만듬 샤히드가 지우삼
    private String countryId;

    // TODO 관계 이미 다 만듬 샤히드가 지우삼
    private long cpId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cp_id")
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id" )
    private UserCountryEntity country;

}
