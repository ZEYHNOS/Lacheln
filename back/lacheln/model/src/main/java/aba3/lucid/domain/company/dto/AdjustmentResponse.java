package aba3.lucid.domain.company.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentResponse {
    private long cpId;
    private aba3.lucid.adjustment.enums.Bank cpBankName;
    private String cpBankAccount;
    private String cpDepositName;
    private String cpReceiptDate;

}
