package aba3.lucid.domain.company;

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

    // TODO ManyToOne 설정
    private String countryId;

    // TODO ManyToOne 설정
    private long cpId;

}
