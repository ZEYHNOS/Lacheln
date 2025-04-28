package aba3.lucid.domain.company.convertor;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdjustmentConvertor {
    public AdjustmentEntity toEntity (AdjustmentRequest request, CompanyEntity company) {
        if(request == null) return null;{

        }

        return AdjustmentEntity.builder()
                .company(company)
                .cpBankName(request.getBankName())
                .cpBankAccount(request.getBankAccount())
                .cpReceiptDate(LocalDate.from(request.getReceiptDate()))
                .cpDepositName(request.getDepositName())
                .build();
    }

    public AdjustmentResponse toResponse(AdjustmentEntity entity) {
        if(entity == null) {
            return null;
        }
        return AdjustmentResponse.builder()
                .cpId(entity.getCompany().getCpId())
                .cpBankName(entity.getCpBankName())
                .cpBankAccount(entity.getCpBankAccount())
                .cpDepositName(entity.getCpDepositName())
                .cpReceiptDate(String.valueOf(entity.getCpReceiptDate()))
                .build();
    }




}
