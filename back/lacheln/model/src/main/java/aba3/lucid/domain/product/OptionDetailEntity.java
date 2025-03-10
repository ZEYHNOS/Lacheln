package aba3.lucid.domain.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@Entity
@Table(name = "option_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDetailEntity {

    @Id
    @Column(name = "op_dt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long opDtId; // 옵션 상세 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_id")
    private OptionEntity option; // 옵션 ID

    @Column(name = "op_dt_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String opDtName; // 옵션 상세 이름

    @Column(name = "op_dt_plus_cost", columnDefinition = "BIGINT", nullable = false)
    private BigInteger opDtPlusCost; // 추가금

    @Column(name = "op_dt_plus_time")
    private int opDtPlusTime; // 추가 시간
}
