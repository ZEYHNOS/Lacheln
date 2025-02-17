package aba3.lucid.cplanmapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cp_lan_mapping")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyLanguageMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cpLanId;

    private String countryId;

    private long cpId;

}
