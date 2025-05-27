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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdjustmentService {

    private final AdjustmentConvertor adjustmentConvertor;
    private final AdjustmentRepository adjustmentRepository;

    @Transactional
    public AdjustmentEntity createAdjustment(AdjustmentEntity entity) {
        AdjustmentEntity savedEntity = adjustmentRepository.save(entity);
        return savedEntity;
    }


    @Transactional
    public AdjustmentEntity updateAdjustment( AdjustmentRequest request, Long cpId) {
        AdjustmentEntity entity = adjustmentRepository.findByCompany_CpId(cpId).orElseThrow(() ->
                new EntityNotFoundException("정보가 없습니다" +cpId));
        //들오는 요청을 해당 entity에 매핑한다
        adjustmentConvertor.updateEntity(entity, request);
        return adjustmentRepository.save(entity);

    }

}
