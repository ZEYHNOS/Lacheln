package aba3.lucid.domain.company.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustmentRequest {
    private aba3.lucid.adjustment.enums.Bank bankName;
    private String bankAccount;
    private String depositName;
    private LocalDateTime receiptDate;

}
