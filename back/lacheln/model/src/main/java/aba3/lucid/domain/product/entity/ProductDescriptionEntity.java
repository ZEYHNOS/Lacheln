package aba3.lucid.domain.product.entity;

import aba3.lucid.common.enums.DescriptionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pd_description")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdDescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DescriptionType type;

    @Column(name = "block_value", nullable = false)
    private String value;

    @Column(name = "block_order")
    private int order;

}
