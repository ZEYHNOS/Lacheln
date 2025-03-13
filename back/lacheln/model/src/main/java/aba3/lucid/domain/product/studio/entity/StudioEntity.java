package aba3.lucid.domain.product.studio.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "studio")
@SuperBuilder
@DiscriminatorValue("studio")
public class StudioEntity extends ProductEntity{

    // 실내촬영여부
    @Column(name = "std_in_available", nullable = false)
    private BinaryChoice stdInAvailable;

    // 야외촬영여부
    @Column(name = "std_out_available", nullable = false)
    private BinaryChoice stdOutAvailable;

    // 최대수용인원
    @Column(name = "std_max_people", nullable = false)
    private int stdMaxPeople;

    // 배경선택여부
    @Column(name = "std_bg_options", nullable = false)
    private BinaryChoice stdBgOptions;
}
