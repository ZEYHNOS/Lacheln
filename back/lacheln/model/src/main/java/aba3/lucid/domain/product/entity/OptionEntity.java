package aba3.lucid.domain.product.entity;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.common.enums.BinaryChoice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "product_option")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionEntity {

    // 상품 옵션
    @Id
    @Column(name = "op_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long opId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_id")
    private ProductEntity product;

    // 옵션 이름
    @Column(name = "op_name", columnDefinition = "VARCHAR(30)", nullable = false)
    private String opName;

    // 중복 여부
    @Enumerated(EnumType.STRING)
    @Column(name = "op_overlap", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice opOverlap;

    // 필수 여부
    @Enumerated(EnumType.STRING)
    @Column(name = "op_essential", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice opEssential;

    // 옵션 상태(활성, 비활성) 사용자에게 노출하지 말 것
    @Enumerated(EnumType.STRING)
    @Column(name = "op_status", columnDefinition = "CHAR(20)", nullable = false)
    private ActiveEnum opStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE)
    private List<OptionDetailEntity> opDtList;
}
