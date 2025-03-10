package aba3.lucid.domain.payment;

import aba3.lucid.adjustment.enums.Bank;
import aba3.lucid.domain.company.CompanyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "adjustment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjustmentEntity {

    // 회사(1:1)
    @Id
    @JoinColumn(name = "cp_id")
    @OneToOne(fetch = FetchType.LAZY)
    private CompanyEntity company;

    // 은행 이름
    @Enumerated(EnumType.STRING)
    @Column(name = "cp_bank_name", columnDefinition = "CHAR(50)", nullable = false)
    private Bank cpBankName;

    // 은행 계좌번호
    @Column(name = "cp_bank_account", columnDefinition = "CHAR(20)", nullable = false)
    private String cpBankAccount;

    // 예금주명
    @Column(name = "cp_deposit_name", columnDefinition = "CHAR(100)", nullable = false)
    private String cpDepositName;

    // 정기 대금 수령일
    @Column(name = "cp_receipt_date", columnDefinition = "DATE", nullable = false)
    private LocalDate cpReceiptDate;
}
