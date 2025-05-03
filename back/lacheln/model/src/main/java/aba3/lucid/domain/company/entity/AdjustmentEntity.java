package aba3.lucid.domain.company.entity;

import aba3.lucid.domain.payment.enums.Bank;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "adjustment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjustmentEntity {

    // 회사(1:1)
    @Id
    @Column(name = "cp_id")
    private long cpId;

    @MapsId
    @JoinColumn(name = "cp_id")
    @OneToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 은행 이름
    @Enumerated(EnumType.STRING)
    @Column(name = "cp_bank_name", columnDefinition = "CHAR(50)", nullable = false)
    private Bank cpBankName;

    // 은행 계좌번호
    @Column(name = "cp_bank_account", columnDefinition = "CHAR(20) DEFAULT 'AABB1254' NOT NULL")
    private String cpBankAccount;

    // 예금주명
    @Column(name = "cp_deposit_name", columnDefinition = "CHAR(100) DEFAULT '박예민' NOT NULL")
    private String cpDepositName;

    // 정기 대금 수령일
    @Column(name = "cp_receipt_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime cpReceiptDate;
}
