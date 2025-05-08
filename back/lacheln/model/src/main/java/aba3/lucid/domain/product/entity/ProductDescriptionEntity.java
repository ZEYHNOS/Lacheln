package aba3.lucid.domain.product.entity;

import aba3.lucid.common.enums.DescriptionType;
import aba3.lucid.domain.description.Description;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "pd_description")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class ProductDescriptionEntity extends Description {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

}
