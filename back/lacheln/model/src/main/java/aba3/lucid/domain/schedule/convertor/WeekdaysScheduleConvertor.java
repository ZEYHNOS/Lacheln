package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
@RequiredArgsConstructor
public class WeekdaysScheduleConvertor {


    public WeekdaysScheduleEntity toEntity(WeekdaysScheduleRequest request, CompanyEntity company) {
       WeekdaysScheduleEntity weekdaysSchedule = WeekdaysScheduleEntity.builder()
               .company(company)
               .wsWeekdays(request.getWeekday())
               .wsEnd(request.getEnd())
               .wsStart(request.getStart())
               .build();
       return weekdaysSchedule;
    }





    public WeekdaysScheduleResponse toResponse(WeekdaysScheduleEntity entity) {
        return WeekdaysScheduleResponse.builder()
                .wsId(entity.getWsId())
                .weekday(entity.getWsWeekdays())
                .start(entity.getWsStart())
                .end(entity.getWsEnd())
                .build();
    }

    //한 DTO ->>> 한 Entity 매필 명화해짐
    public void updateEntity (WeekdaysScheduleRequest request, WeekdaysScheduleEntity entity) {
        entity.setWsWeekdays(request.getWeekday());
        entity.setWsStart(request.getStart());
        entity.setWsEnd(request.getEnd());

    }





}
