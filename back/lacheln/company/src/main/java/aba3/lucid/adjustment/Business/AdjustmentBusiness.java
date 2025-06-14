package aba3.lucid.adjustment.Business;


import aba3.lucid.adjustment.Service.AdjustmentService;
import aba3.lucid.common.annotation.Business;
import aba3.lucid.common.validate.Validator;
import aba3.lucid.company.service.CompanyService;
import aba3.lucid.domain.company.converter.AdjustmentConverter;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Business
@Component
@RequiredArgsConstructor
public class AdjustmentBusiness {

    private final AdjustmentConverter adjustmentConverter;
    private final CompanyService companyService;
    private final AdjustmentService adjustmentService;

    public AdjustmentResponse createAdjustmentEntity(AdjustmentRequest request, Long cpId) {
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(cpId);
        CompanyEntity company = companyService.findByIdWithThrow(cpId);

        //새로운 adjustmentEntity 만들고 업체 해당하기
        AdjustmentEntity entity = adjustmentConverter.toEntity(request,cpId);
        entity.setCompany(company);
        AdjustmentEntity savedEntity = adjustmentService.createAdjustment(entity);
        return adjustmentConverter.toResponse(savedEntity);
    }



    public AdjustmentResponse updateAdjustment(AdjustmentRequest request, Long cpId){
        Validator.throwIfNull(request);
        Validator.throwIfInvalidId(cpId);
        //업체가 존재하는지 확인
        companyService.findByIdWithThrow(cpId);
        //해당 업체 대한 정산을 수정하기
        AdjustmentEntity updatedEntity = adjustmentService.updateAdjustment(request,cpId);
        //convert DTO로 바꾸기
        return adjustmentConverter.toResponse(updatedEntity);


    }


}
