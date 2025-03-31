package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeekdaysScheduleConvertor {
    //WeekdaysScheduleRequest를 List<WeekdaysScheduleEntity로 변환

    public List<WeekdaysScheduleEntity> toEntityList(WeekdaysScheduleRequest request, CompanyEntity company) {
        List<WeekdaysScheduleEntity> entities = new ArrayList<>();
//        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        for(WeekdaysScheduleRequest.DayScheduleDto dto : request.getScheduleList()) {
            // // 입력받은 요일 문자열("MON", "TUE" 등)을 enum으로 변환 (대문자 변환)
            Weekdays weekdayEnum = Weekdays.valueOf(dto.getWeekday().toUpperCase());
            LocalTime startTime = LocalTime.parse(dto.getStart());
            LocalTime endTime = LocalTime.parse(dto.getEnd());

            WeekdaysScheduleEntity entity = WeekdaysScheduleEntity.builder()
                    .company(company)
                    .wsWeekdays(weekdayEnum)
                    .wsEnd(endTime)
                    .wsStart(startTime)
                    .build();
            entities.add(entity);
        }
        return entities;
    }
    //List<WeekdaysScheduleEntity>를 List<WeekdaysScheduleResponse> DTO로 변환
    public List<WeekdaysScheduleResponse> toResponseList(List<WeekdaysScheduleEntity> entities) {
        return entities.stream().map(entity -> new WeekdaysScheduleResponse(
                entity.getWsId(),
                entity.getWsWeekdays().name(),
                entity.getWsStart().toString(),
                entity.getWsEnd().toString()
        )).collect(Collectors.toList());
    }
}
