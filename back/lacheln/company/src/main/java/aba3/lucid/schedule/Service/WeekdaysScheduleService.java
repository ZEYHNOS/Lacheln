package aba3.lucid.schedule.Service;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeekdaysScheduleService {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;

    @Transactional
    public List<WeekdaysScheduleEntity> createWeekdaysSchedule(List<WeekdaysScheduleEntity> entities) {
        return weekdaysScheduleRepository.saveAll(entities);
    }

    @Transactional
    public List<WeekdaysScheduleEntity> updateWeekdaysSchedule(Long wsId, WeekdaysScheduleRequest.DayScheduleDto dtoList) {
        WeekdaysScheduleEntity entity = weekdaysScheduleRepository.findById(wsId).orElseThrow(()
                -> new ApiException(ErrorCode.NOT_FOUND));
        weekdaysScheduleConvertor.updateEntity(dtoList, entity);
        return Collections.singletonList(weekdaysScheduleRepository.save(entity));

    }

    public WeekdaysScheduleEntity findByThrowId(long wsId) {
        return weekdaysScheduleRepository.findByWsId(wsId).orElseThrow(() ->
                new ApiException(ErrorCode.NOT_FOUND, " WeekdaysScheduleEntity 정보가 없습니다" + wsId));
    }

    public List<WeekdaysScheduleEntity> findAllByCompanyId(Long companyId) {
        return weekdaysScheduleRepository.findAllByCompany_CpId(companyId);
    }

    public WeekdaysScheduleEntity findByCompanyIdAndWsWeekdays(Long companyId, Weekdays weekdays) {
        return weekdaysScheduleRepository.findByCompany_CpIdAndWsWeekdays(companyId, weekdays)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND));
    }
}
