package aba3.lucid.domain.product.makeup.entity;

import aba3.lucid.common.enums.BinaryChoice;
import aba3.lucid.domain.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "makeup")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MakeUpEntity {

    @Id
    @Column(name = "makeup_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long makeupId;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

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
