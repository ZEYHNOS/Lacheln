package aba3.lucid.domain.company.dto;


import aba3.lucid.domain.payment.enums.Bank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentRequest {
    private Bank bankName;
    private String bankAccount;
    private String depositName;
    private LocalDateTime receiptDate;

}
