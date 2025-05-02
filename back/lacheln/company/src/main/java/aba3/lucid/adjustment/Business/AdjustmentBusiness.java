package aba3.lucid.adjustment.Business;


import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.convertor.AdjustmentConvertor;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.AdjustmentRepository;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.payment.enums.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class AdjustmentBusiness {

    public AdjustmentEntity createAdjustmentEntity(AdjustmentRequest request, CompanyEntity company) {
        Bank bankName = (request.getBankName() != null) ? (request.getBankName()): Bank.NH_BANK;
        String bankAccount = (request.getBankAccount() != null) ? (request.getBankAccount()) : "ABS7964";
        String depositName = (request.getDepositName() != null) ? (request.getDepositName()) : "Bank";
        LocalDateTime receiptDate = (request.getReceiptDate() != null) ? (request.getReceiptDate()) : LocalDateTime.now();
        return AdjustmentEntity.builder()
                .company(company)
                .cpBankName(bankName)
                .cpBankAccount(bankAccount)
                .cpDepositName(depositName)
                .cpReceiptDate(LocalDate.from(receiptDate))
                .build();
    }



    public void applyUpdates(AdjustmentRequest request, AdjustmentEntity entity){
       if(request.getBankName() != null) entity.setCpBankName(request.getBankName());
       if(request.getBankAccount() != null) entity.setCpBankAccount(request.getBankAccount());
       if(request.getDepositName() != null) entity.setCpDepositName(request.getDepositName());
       if(request.getReceiptDate() != null) entity.setCpReceiptDate(LocalDate.from(request.getReceiptDate()));

    }


}
