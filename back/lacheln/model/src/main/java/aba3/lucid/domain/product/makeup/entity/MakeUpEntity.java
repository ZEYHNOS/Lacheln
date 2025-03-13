package aba3.lucid.domain.product.makeup.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Table(name = "makeup")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("studio")
public class MakeUpEntity extends ProductEntity {

    // 출장 여부
    @Column(name = "makeup_bt", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice makeupBt;

    // 방문 여부
    @Column(name = "makeup_visit", nullable = false)
    @Enumerated(EnumType.STRING)
    private BinaryChoice makeupVisit;

    // 담당자
    @Column(name = "makeup_manager", nullable = false, columnDefinition = "VARCHAR(10)")
    private String makeupManager;

}
