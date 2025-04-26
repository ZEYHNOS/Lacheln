package aba3.lucid.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@Entity
@Table(name = "pay_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayDetailEntity {

    @Id
    @Column(name = "pay_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payDetailId;

    @JoinColumn(name = "pay_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PayManagementEntity payManagement;

    // 옵션 이름
    @Column(name = "pay_op_name", columnDefinition = "VARCHAR(30)", nullable = false)
    private String payOpName;

    // 옵션 상세 이름
    @Column(name = "pay_op_dt_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String payOpDtName;

    // 수량
    @Column(name = "pay_dt_quantity", nullable = false)
    private int payDtQuantity;

    // 추가 금액
    @Column(name = "pay_op_plus_cost", nullable = false)
    private BigInteger payOpPlusCost;
}
