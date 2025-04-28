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

@Business
@RequiredArgsConstructor
@Component
public class AdjustmentBusiness {
    private final AdjustmentRepository adjustmentRepository;
    private final AdjustmentConvertor adjustmentConvertor;
    private final CompanyRepository companyRepository;

    public AdjustmentResponse createAdjustment(AdjustmentRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다." + cpId)
        );
        Bank bankName = (request.getBankName() != null) ? (request.getBankName()): Bank.NH_BANK;
        String bankAccount = (request.getBankAccount() != null) ? (request.getBankAccount()) : "ABS7964";
        String depositName = (request.getDepositName() != null) ? (request.getDepositName()) : "Bank";
        LocalDateTime receiptDate = (request.getReceiptDate() != null) ? (request.getReceiptDate()) : LocalDateTime.now();

        AdjustmentRequest defaultRequest = new AdjustmentRequest( bankName,bankAccount, depositName, receiptDate);
        AdjustmentEntity entity = adjustmentConvertor.toEntity(defaultRequest,company);
        AdjustmentEntity savedEntity = adjustmentRepository.save(entity);
        return adjustmentConvertor.toResponse(savedEntity);
    }

    @Transactional
    public AdjustmentResponse updateAdjustment(AdjustmentRequest request, Long cpId){
        AdjustmentEntity adjustmentEntity = adjustmentRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다" + cpId)
        );

        adjustmentEntity.setCpBankName(request.getBankName());
        adjustmentEntity.setCpBankAccount(request.getBankAccount());
        adjustmentEntity.setCpDepositName(request.getDepositName());
        adjustmentEntity.setCpReceiptDate(LocalDate.from(request.getReceiptDate()));
        AdjustmentEntity updatedEntity = adjustmentRepository.save(adjustmentEntity);
        return adjustmentConvertor.toResponse(updatedEntity);


    }


}
