package aba3.lucid.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "option_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OptionDetailEntity {

    @Id
    @Column(name = "op_dt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long opDtId; // 옵션 상세 ID

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_id")
    private OptionEntity option; // 옵션 ID

    @Column(name = "op_dt_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String opDtName; // 옵션 상세 이름

    @Column(name = "op_dt_plus_cost", columnDefinition = "BIGINT", nullable = false)
    private BigInteger opDtPlusCost; // 추가금

    @Column(name = "op_dt_plus_time")
    private LocalTime opDtPlusTime; // 추가 시간
}
