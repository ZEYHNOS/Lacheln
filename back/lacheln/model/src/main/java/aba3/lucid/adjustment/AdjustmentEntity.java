package aba3.lucid.adjustment;

import aba3.lucid.adjustment.enums.Bank;
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

    // 업체 id
    // TODO oneToOne 처리하기
    @Id
    private long cpId;

    // 은행 이름
    @Enumerated(EnumType.STRING)
    @Column(name = "cp_bank_name", columnDefinition = "CHAR(50)", nullable = false)
    private Bank bankName;

    // 은행 계좌번호
    @Column(name = "cp_bank_account", columnDefinition = "CHAR(20)", nullable = false)
    private String bankAccount;

    // 예금주명
    @Column(name = "cp_deposit_name", columnDefinition = "CHAR(100)", nullable = false)
    private String depositName;

    // 정기 대금 수령일
    @Column(name = "cp_receipt_date", columnDefinition = "DATE", nullable = false)
    private LocalDate receiptDate;

}
