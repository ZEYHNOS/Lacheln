package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "popular_product")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PopularEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long popularId;

    private Long companyId;

    private Long productId;

    private Integer popularRank;

}
