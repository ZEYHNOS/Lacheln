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

    private  static final String DEFAULT_START_TIME = "09:00";
    private static final String DEFAULT_END_TIME = "18:00";
    private static final String DAY_OFF = "휴무";

    private final TemporaryHolidayRepository temporaryHolidayRepository;

    public WeekdaysScheduleConvertor(TemporaryHolidayRepository temporaryHolidayRepository) {
        this.temporaryHolidayRepository = temporaryHolidayRepository;
    }

    //WeekdaysScheduleRequest를 List<WeekdaysScheduleEntity로 변환

    public List<WeekdaysScheduleEntity> toEntity(WeekdaysScheduleRequest request, CompanyEntity cp_Id) {
        if(request == null || request.getScheduleList() == null) {
            return null;

        }
        List<WeekdaysScheduleEntity> entityList = new ArrayList<>();
        for(WeekdaysScheduleRequest.DayScheduleDto dto : request.getScheduleList()) {
            WeekdaysScheduleEntity entity = WeekdaysScheduleEntity.builder()
                    .company(cp_Id)
                    .wsWeekdays(Weekdays.valueOf(dto.getWeekday()))
                    .wsStart(parseTime(dto.getStart(), DEFAULT_START_TIME))
                    .wsEnd(parseTime(dto.getEnd(), DEFAULT_END_TIME))
                    .build();

            if(DAY_OFF.equals(dto.getWeekday())) {
                entity.setWsStart(null);
                entity.setWsEnd(null);

                TemporaryHolidayEntity holidayEntity = new TemporaryHolidayEntity();
                holidayEntity.setCompany(cp_Id);
                holidayEntity.setThDate(LocalDate.parse(dto.getStart()));
                holidayEntity.setThReason("재등록");
                temporaryHolidayRepository.save(holidayEntity);

            }
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
