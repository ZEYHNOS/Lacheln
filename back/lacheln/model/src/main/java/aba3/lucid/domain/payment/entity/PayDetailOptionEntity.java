package aba3.lucid.domain.payment.entity;

import aba3.lucid.domain.product.enums.DressSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "pay_detail_option")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayDetailOptionEntity {

    @Id
    @Column(name = "pay_detail_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payDetailOptionId;

    @JoinColumn(name = "pay_detail_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PayDetailEntity payDetail;

    // 옵션 이름
    @Column(name = "pay_op_name", columnDefinition = "VARCHAR(30)", nullable = false)
    private String payOpName;

    // 옵션 상세 이름
    @Column(name = "pay_op_dt_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String payOpDtName;

    // 드레스 사이즈
    @Enumerated(EnumType.STRING)
    @Column(name = "dress_size", columnDefinition = "CHAR(3)")
    private DressSize dressSize;

    // 수량
    @Column(name = "pay_dt_quantity", nullable = false)
    private int payDtQuantity;

    // 추가 금액
    @Column(name = "pay_op_plus_cost", nullable = false)
    private BigInteger payOpPlusCost;
    
    // 옵션 소요 시간
    @Column(name = "pay_op_task_time", nullable = false)
    private LocalTime payOpTaskTime;

    public void updateDressSize(String size) {
        this.dressSize = DressSize.valueOf(size);
    }
}
