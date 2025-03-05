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
    private long opDtId;

    @ManyToOne
    @JoinColumn(name = "op_id")
    private OptionEntity option;

    @Column(name = "op_dt_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String opDtName;

    @Column(name = "op_dt_plus_cost", columnDefinition = "BIGINT", nullable = false)
    private BigInteger opDtPlusCost;

}
