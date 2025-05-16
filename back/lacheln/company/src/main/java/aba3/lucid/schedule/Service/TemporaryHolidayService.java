package aba3.lucid.schedule.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.convertor.TemporaryHolidayConvertor;
import aba3.lucid.domain.schedule.dto.TemporaryHolidayRequest;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemporaryHolidayService {

    private final TemporaryHolidayRepository temporaryHolidayRepository;
    private final TemporaryHolidayConvertor temporaryHolidayConvertor;


    @Transactional
    public TemporaryHolidayEntity createTemporaryHoliday(TemporaryHolidayEntity temEntity) {
        TemporaryHolidayEntity savedEntity = temporaryHolidayRepository.save(temEntity);
        return savedEntity;
    }

    @Transactional
    public TemporaryHolidayEntity updateTemporaryHoliday(TemporaryHolidayRequest request, Long temHolidayId) {
        TemporaryHolidayEntity temporaryHolidayEntity = temporaryHolidayRepository.findByTemHolidayId(temHolidayId).orElseThrow(() ->
                new EntityNotFoundException("TemporaryHoliday 정보가  없습니다"));
        temporaryHolidayConvertor.updateEntity(temporaryHolidayEntity, request);
        return temporaryHolidayRepository.save(temporaryHolidayEntity);

    }

    public TemporaryHolidayEntity findByIdWithThrow( long temHolidayId) {
        return temporaryHolidayRepository.findByTemHolidayId(temHolidayId).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND,
                "TemporaryHolidayEntity 정보기 없습니다"));
    }

    public List<TemporaryHolidayEntity> findAllByCompanyId(Long companyId) {
        return temporaryHolidayRepository.findAllByCompany_CpId(companyId);
    }


    public boolean existsByCompanyIdAndDate(Long companyId, LocalDate date) {
        return temporaryHolidayRepository.existsByCompany_CpIdAndThDate(companyId, date);
    }

}
