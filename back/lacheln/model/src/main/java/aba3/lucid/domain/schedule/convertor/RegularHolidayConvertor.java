package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegularHolidayConvertor {

    public RegularHolidayEntity toEntity(RegularHolidayRequest request,  Long cpId) {

        return RegularHolidayEntity.builder()
                .rhHdWeekdays(request.getWeekdays())
                .rhHdWeek(request.getWeek())
                .rhHdDays(request.getDays())
                .rhHdMonth(request.getMonths())
                .build();
    }

    public RegularHolidayResponse toResponse(RegularHolidayEntity entity) {

        return RegularHolidayResponse.builder()
                .id(entity.getRegHolidayId())
                .cpId(entity.getCompany().getCpId())
                .weekdays(entity.getRhHdWeekdays())
                .days(entity.getRhHdDays())
                .month(entity.getRhHdMonth())
                .week(entity.getRhHdWeek())
                .build();
    }

    public void updateEntity(RegularHolidayEntity entity, RegularHolidayRequest request) {
        entity.setRhHdWeekdays(request.getWeekdays());
        entity.setRhHdWeek(request.getWeek());
        entity.setRhHdDays(request.getDays());
        entity.setRhHdMonth(request.getMonths());
    }

}
