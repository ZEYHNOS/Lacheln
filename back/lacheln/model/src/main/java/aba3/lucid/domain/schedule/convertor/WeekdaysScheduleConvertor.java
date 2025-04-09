package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.TemporaryHolidayEntity;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import aba3.lucid.domain.schedule.repository.TemporaryHolidayRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeekdaysScheduleConvertor {


    public List<WeekdaysScheduleEntity> toEntity(WeekdaysScheduleRequest request, CompanyEntity cp_Id) {
        if(request == null || request.getScheduleList() == null) {
            return null;

        }
        List<WeekdaysScheduleEntity> entityList = new ArrayList<>();
        for(WeekdaysScheduleRequest.DayScheduleDto dto : request.getScheduleList()) {
            WeekdaysScheduleEntity entity = WeekdaysScheduleEntity.builder()
                    .company(cp_Id)
                    .wsWeekdays(Weekdays.valueOf(dto.getWeekday()))
                    .wsStart(LocalTime.parse(dto.getStart()))
                    .wsEnd(LocalTime.parse(dto.getEnd()))
                    .build();

            entityList.add(entity);

        }
        return entityList;

    }
    private LocalTime parseTime(String time, String defaultTime ) {
        if(time == null || time.isEmpty()) {
            return LocalTime.parse(defaultTime);
        }
        return  LocalTime.parse(time);

    }

    // Convert WeekdaysScheduleEntity to WeekdaysScheduleResponse
    public WeekdaysScheduleResponse toResponse(WeekdaysScheduleEntity entity) {
        if(entity == null) {
            return null;
        }

        return new WeekdaysScheduleResponse(
                entity.getWsId(),
                entity.getWsWeekdays().name(),
                entity.getWsStart() != null ? entity.getWsStart().toString():"휴무",
                entity.getWsEnd() != null ? entity.getWsEnd().toString(): "휴무"
        );
    }
}
