package aba3.lucid.adjustment.Service;


import aba3.lucid.adjustment.Business.AdjustmentBusiness;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.convertor.AdjustmentConvertor;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.dto.AdjustmentResponse;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.AdjustmentRepository;
import aba3.lucid.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdjustmentService {

    private final CompanyRepository companyRepository;
    private final AdjustmentConvertor adjustmentConvertor;
    private final AdjustmentRepository adjustmentRepository;
    private final AdjustmentBusiness adjustmentBusiness;

    @Transactional
    public AdjustmentResponse createAdjustment(AdjustmentRequest request, Long cpId) {
        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다: " + cpId));

        AdjustmentEntity toSave = adjustmentConvertor.toEntity(request,company);

        AdjustmentEntity saved = adjustmentRepository.save(toSave);
        return adjustmentConvertor.toResponse(saved);
    }

    @Transactional
    public AdjustmentResponse updateAdjustment(AdjustmentRequest request, Long cpId) {
        AdjustmentEntity entity = adjustmentRepository.findById(cpId).orElseThrow(
                () -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다" + cpId)
        );
        adjustmentBusiness.applyUpdates(request, entity);
        AdjustmentEntity updated = adjustmentRepository.save(entity);
        return  adjustmentConvertor.toResponse(updated);

    }

}
