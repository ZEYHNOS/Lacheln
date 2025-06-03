package aba3.lucid.domain.schedule.converter;


import aba3.lucid.common.annotation.Converter;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleRequest;
import aba3.lucid.domain.schedule.dto.WeekdaysScheduleResponse;
import aba3.lucid.domain.schedule.entity.WeekdaysScheduleEntity;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class WeekdaysScheduleConverter {


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
