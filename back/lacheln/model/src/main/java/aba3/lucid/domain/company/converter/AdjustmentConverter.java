package aba3.lucid.domain.company.converter;


import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdjustmentConverter {
    public AdjustmentEntity toEntity (AdjustmentRequest request, Long cpId) {
        if(request == null) return null;{

        }

        return AdjustmentEntity.builder()
                .cpBankName(request.getBankName())
                .cpBankAccount(request.getBankAccount())
                .cpReceiptDate(LocalDateTime.of(2019, 10, 12, 0, 0))
                .cpDepositName(request.getDepositName())
                .build();
    }

    public AdjustmentResponse toResponse(AdjustmentEntity entity) {
        if(entity == null) {
            return null;
        }
        return AdjustmentResponse.builder()
                .cpId(entity.getCpId())
                .cpBankName(entity.getCpBankName())
                .cpBankAccount(entity.getCpBankAccount())
                .cpDepositName(entity.getCpDepositName())
                .cpReceiptDate(entity.getCpReceiptDate())
                .build();
    }

    public void updateEntity(AdjustmentEntity entity, AdjustmentRequest request) {
        entity.setCpBankName(request.getBankName());
        entity.setCpBankAccount(request.getBankAccount());
        entity.setCpDepositName(request.getDepositName());
        entity.setCpReceiptDate(request.getReceiptDate());
    }


}
