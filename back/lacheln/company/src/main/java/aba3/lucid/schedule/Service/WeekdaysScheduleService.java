package aba3.lucid.schedule.Service;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.BusinessHourResponse;
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
    public WeekdaysScheduleEntity createWeekdaysSchedule(WeekdaysScheduleEntity entity) {
        return weekdaysScheduleRepository.save(entity);
    }

    @Transactional
    public WeekdaysScheduleEntity updateWeekdaysSchedule(Long wsId,  WeekdaysScheduleRequest request) {
        WeekdaysScheduleEntity entity = weekdaysScheduleRepository.findById(wsId).orElseThrow(()
                -> new ApiException(ErrorCode.NOT_FOUND));
       weekdaysScheduleConvertor.updateEntity(request, entity);
       return weekdaysScheduleRepository.save(entity);

    }

    // [일,월,화,수,목,금,토] 영업 시간 리스트
    public BusinessHourResponse[] getBusinessHour(Long companyId) {
        BusinessHourResponse[] result = new BusinessHourResponse[7];

        List<WeekdaysScheduleEntity> weekdaysScheduleEntityList = findAllByCompanyId(companyId);
        for (WeekdaysScheduleEntity entity : weekdaysScheduleEntityList) {
            result[entity.getWsWeekdays().getValue()-1] = BusinessHourResponse.builder()
                    .start(entity.getWsStart())
                    .end(entity.getWsEnd())
                    .build()
            ;
        }

        return result;
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
