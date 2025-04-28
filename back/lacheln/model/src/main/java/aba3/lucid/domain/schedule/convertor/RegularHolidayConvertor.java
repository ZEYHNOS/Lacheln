package aba3.lucid.domain.schedule.convertor;


import aba3.lucid.common.enums.HolidayWeek;
import aba3.lucid.common.enums.Weekdays;
import aba3.lucid.domain.company.entity.CompanyEntity;
import aba3.lucid.domain.schedule.dto.RegularHolidayRequest;
import aba3.lucid.domain.schedule.dto.RegularHolidayResponse;
import aba3.lucid.domain.schedule.entity.RegularHolidayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegularHolidayConvertor {

    public RegularHolidayEntity toEntity(RegularHolidayRequest request, CompanyEntity company) {

        return RegularHolidayEntity.builder()
                .cpId(company)
                .rhHdWeekdays(Weekdays.valueOf(request.getWeekdays()))
                .rhHdWeek(HolidayWeek.valueOf(request.getWeek()))
                .rhHdDays(request.getDays())
                .rhHdMonth(request.getMonths())
                .build();
    }

    public RegularHolidayResponse toResponse(RegularHolidayEntity entity) {

        return RegularHolidayResponse.builder()
                .id(entity.getRegHolidayId())
                .cpId(entity.getCpId().getCpId())
                .weekdays(entity.getRhHdWeekdays().toString())
                .days(entity.getRhHdDays())
                .month(entity.getRhHdMonth())
                .week(entity.getRhHdWeek().toString())
                .build();
    }

}
