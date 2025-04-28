package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.payment.enums.Bank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentResponse {
    private long cpId;
    private Bank cpBankName;
    private String cpBankAccount;
    private String cpDepositName;
    private String cpReceiptDate;

}
