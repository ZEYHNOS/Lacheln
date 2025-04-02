package aba3.lucid.schedule.Business;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.common.exception.ApiException;
import aba3.lucid.common.status_code.ErrorCode;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.company.repository.CompanyRepository;
import aba3.lucid.domain.schedule.convertor.WeekdaysScheduleConvertor;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import aba3.lucid.domain.schedule.repository.WeekdaysScheduleRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeekdaysScheduleBusiness {
    private final WeekdaysScheduleRepository weekdaysScheduleRepository;
    private final TemporaryHolidayRepository temporaryHolidayRepository;
    private final WeekdaysScheduleConvertor weekdaysScheduleConvertor;
    private final CompanyRepository companyRepository;

    public WeekdaysScheduleBusiness(WeekdaysScheduleConvertor weekdaysScheduleConvertor,
                                    WeekdaysScheduleRepository weekdaysScheduleRepository,
                                    TemporaryHolidayRepository temporaryHolidayRepository,
                                    CompanyRepository companyRepository) {
        this.weekdaysScheduleRepository = weekdaysScheduleRepository;
        this.weekdaysScheduleConvertor = weekdaysScheduleConvertor;
        this.temporaryHolidayRepository = temporaryHolidayRepository;
        this.companyRepository = companyRepository;
    }
    public List<WeekdaysScheduleResponse> saveWeekdaysSchedule(WeekdaysScheduleRequest request, long cpId) {
        if(request == null) {
            throw  new ApiException(ErrorCode.BAD_REQUEST, "WeekdaysRequest 값을 못 받았습니다.");
        }

        CompanyEntity company = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "업체를 찾을 수 없습니다"));



        List<WeekdaysScheduleEntity> weekdaysScheduleEntities = weekdaysScheduleConvertor.toEntity(request, company);

        for (WeekdaysScheduleEntity entity : weekdaysScheduleEntities) {
            switch (entity.getWsWeekdays()) {
                case MON:
                case TUE:
                case WED:
                case THU:
                case FRI:
                    if( entity.getWsStart() == null) {
                        entity.setWsStart(LocalTime.parse("09:00"));
                    }
                    if( entity.getWsEnd() == null) {
                        entity.setWsEnd(LocalTime.parse("18:00"));
                    }
                    break;
                case SAT:
                    if (entity.getWsStart() == null) {
                        // If no start time is provided, set default time
                        entity.setWsStart(LocalTime.parse("10:00"));
                    }
                    if (entity.getWsEnd() == null) {
                        // If no end time is provided, set default time
                        entity.setWsEnd(LocalTime.parse("20:00"));
                    }
                    break;
                case SUN:
                    if(entity.getWsStart() == null) {
                        entity.setWsStart(LocalTime.parse("10:00"));
                    }
                    if(entity.getWsEnd() == null) {
                        entity.setWsEnd(LocalTime.parse("18:00"));
                    }
                    break;
                default:
                    break;
            }
            if(entity.getWsWeekdays() == null) {
                entity.setWsWeekdays(Weekdays.MON);
            }
        }

        List<WeekdaysScheduleEntity> savedEntities = weekdaysScheduleRepository.saveAll(weekdaysScheduleEntities);

        return savedEntities.stream()
                .map(weekdaysScheduleConvertor:: toResponse)
                .collect(Collectors.toList());

    }

    public WeekdaysScheduleResponse updateWeekdaysSchedule(WeekdaysScheduleRequest request, long cpId, long wsId) {
        WeekdaysScheduleEntity weekdaysScheduleEntity = weekdaysScheduleRepository.findByWsId(wsId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "정보가 없습니다"));


        CompanyEntity companyEntity = companyRepository.findById(cpId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "회사 정보를 찾을 수 없습니다. "));

        if(request.getScheduleList() == null || request.getScheduleList().isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "일정 목록이 비었습니다");
        }
        for(WeekdaysScheduleRequest.DayScheduleDto scheduleDto : request.getScheduleList()) {
            if(weekdaysScheduleEntity.getWsWeekdays().equals(scheduleDto.getWeekday())) {
                if(scheduleDto.getStart()!= null) {
                    weekdaysScheduleEntity.setWsStart(LocalTime.parse(scheduleDto.getStart()));
                }
                if(scheduleDto.getEnd()!= null) {
                    weekdaysScheduleEntity.setWsEnd(LocalTime.parse(scheduleDto.getEnd()));
                }

            }
        }

        WeekdaysScheduleEntity updatedEntity = weekdaysScheduleRepository.save(weekdaysScheduleEntity);
        return  weekdaysScheduleConvertor.toResponse(updatedEntity);

    }



}
