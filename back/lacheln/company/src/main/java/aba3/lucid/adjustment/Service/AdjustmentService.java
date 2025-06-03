package aba3.lucid.adjustment.Service;


import aba3.lucid.domain.company.converter.AdjustmentConverter;
import aba3.lucid.domain.company.dto.AdjustmentRequest;
import aba3.lucid.domain.company.entity.AdjustmentEntity;
import aba3.lucid.domain.company.repository.AdjustmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdjustmentService {

    private final AdjustmentConverter adjustmentConverter;
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
        adjustmentConverter.updateEntity(entity, request);
        return adjustmentRepository.save(entity);

    }

}
