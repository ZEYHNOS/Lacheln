package aba3.lucid.schedule.Service;


import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.convertor.RegularHolidayConvertor;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import aba3.lucid.domain.schedule.repository.RegularHolidayRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegularHolidayService {


    private final RegularHolidayRepository regularHolidayRepository;
    private final RegularHolidayConvertor regularHolidayConvertor;

    @Transactional
    public RegularHolidayEntity createRegularHoliday(RegularHolidayEntity regularHolidayEntity) {
        RegularHolidayEntity savedEntity = regularHolidayRepository.save(regularHolidayEntity);
        return savedEntity;
    }


    @Transactional
    public RegularHolidayEntity updateRegularHoliday(RegularHolidayRequest request, long regularHolidayId) {
        RegularHolidayEntity regularHolidayEntity = regularHolidayRepository.findByRegHolidayId(regularHolidayId).
                orElseThrow(()
                -> new ApiException(ErrorCode.NOT_FOUND));
        //들오는 요청을 해당 entity에 매핑한다
        regularHolidayConvertor.updateEntity(regularHolidayEntity, request);
        return regularHolidayRepository.save(regularHolidayEntity);
    }


    public RegularHolidayEntity findByThrowId(long regularHolidayEntityId ) {
        return regularHolidayRepository.findByRegHolidayId(regularHolidayEntityId).orElseThrow(()->
                new ApiException(ErrorCode.NOT_FOUND, "RegularHolidayEntity 정보가 없습니다" +regularHolidayEntityId));
    }
}
