package aba3.lucid.domain.product;

import aba3.lucid.common.enums.ActiveEnum;
import aba3.lucid.common.enums.BinaryChoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "option")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionEntity {

    // 상품 옵션
    @Id
    @Column(name = "op_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long opId;

    // ManyToOne
    private long pdId;

    // 옵션 이름
    @Column(name = "op_name", columnDefinition = "VARCHAR(30)", nullable = false)
    private String opName;

    // 중복 여부
    @Column(name = "op_overlap", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice opOverlap;

    // 필수 여부
    @Column(name = "op_essential", columnDefinition = "CHAR(1)", nullable = false)
    private BinaryChoice opEssential;

    // 옵션 상태(활성, 비활성)
    @Column(name = "op_status", columnDefinition = "CHAR(20)", nullable = false)
    private ActiveEnum opStatus;
}
