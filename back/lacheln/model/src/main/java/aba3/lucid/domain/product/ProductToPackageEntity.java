package aba3.lucid.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "package_mapping")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductToPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long packMapId;

    // ManyToOne
    private long pdId;

    // ManyToOne
    private long packId;

}
